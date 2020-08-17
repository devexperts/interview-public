package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.error.model.NotEnoughMoneyException;
import com.devexperts.error.model.NotValidAmountException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
public class AccountServiceImplTest {

    @Test
    public void transferSingleThreadTest() throws NotEnoughMoneyException {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testSurname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testSurname_2", BigDecimal.valueOf(100l));
        BigDecimal amount = BigDecimal.valueOf(99.999);
        AccountServiceImpl accountService = new AccountServiceImpl();

        accountService.createAccount(first);
        accountService.createAccount(second);
        accountService.transfer(first, second, amount);

        Assertions.assertEquals(BigDecimal.valueOf(0.001), first.getBalance());
        Assertions.assertEquals(BigDecimal.valueOf(199.999), second.getBalance());
    }

    @Test
    /*
    1 -> 2 50
    2 -> 1 40
    3 -> 1 30
    4 -> 2 20
    2 -> 4 10
     */
    public void transferMultipleThreadsTest() throws InterruptedException {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        UUID thirdAccount = UUID.fromString("f506927e-0990-472a-a73d-a04a16d8e437");
        UUID fourthAccount = UUID.fromString("25f65ea7-a94b-41e1-bb45-3c010edef442");
        Account first = new Account(AccountKey.valueOf(firstAccount),
                "testName_1",
                "testSurname_1",
                BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount),
                "testName_2",
                "testSurname_2",
                BigDecimal.valueOf(100l));
        Account third = new Account(AccountKey.valueOf(thirdAccount),
                "testName_3",
                "testSurname_3",
                BigDecimal.valueOf(100l));
        Account fourth = new Account(AccountKey.valueOf(fourthAccount),
                "testName_4",
                "testSurname_4",
                BigDecimal.valueOf(100l));

        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.createAccount(first);
        accountService.createAccount(second);
        accountService.createAccount(third);
        accountService.createAccount(fourth);

        int threadSize = 5;
        CountDownLatch startThreads = new CountDownLatch(1);
        CountDownLatch endThreads = new CountDownLatch(threadSize);
        IntStream.range(0, threadSize)
                .forEach(index -> {
                    new Thread(() -> {
                        try {
                            startThreads.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        processRunnable(index, first, second, third, fourth, accountService);
                        endThreads.countDown();
                    }).start();
                });
        startThreads.countDown();
        endThreads.await();

        //check result
        BigDecimal expectedFirstAccountBalance = BigDecimal.valueOf(120l);
        BigDecimal expectedSecondAccountBalance = BigDecimal.valueOf(120l);
        BigDecimal expectedThirdAccountBalance = BigDecimal.valueOf(70);
        BigDecimal expectedFourthAccountBalance = BigDecimal.valueOf(90);

        Assertions.assertEquals(expectedFirstAccountBalance, first.getBalance());
        Assertions.assertEquals(expectedSecondAccountBalance, second.getBalance());
        Assertions.assertEquals(expectedThirdAccountBalance, third.getBalance());
        Assertions.assertEquals(expectedFourthAccountBalance, fourth.getBalance());

    }

    @Test
    public void transferNotEnoughMoneyTest() {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        BigDecimal amount = BigDecimal.valueOf(100.00001);
        AccountServiceImpl accountService = new AccountServiceImpl();

        boolean exceptionThrown = false;
        try {
            accountService.transfer(first, second, amount);
        } catch (NotEnoughMoneyException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void transferNullArgumentsTest() throws NotEnoughMoneyException {
        UUID firstAccount = null;
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        BigDecimal amount = BigDecimal.valueOf(123213.123);
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();
        boolean exceptionThrown = false;
        try {
            accountService.transfer(first, second, amount);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);

        firstAccount = UUID.randomUUID();
        first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        amount = null;
        exceptionThrown = false;
        try {
            accountService.transfer(first, second, amount);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);

        first = null;
        amount = BigDecimal.valueOf(123l);
        exceptionThrown = false;
        try {
            accountService.transfer(first, second, amount);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void transferNegativeAmountTest() throws NotEnoughMoneyException {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();
        boolean exceptionThrown = false;
        try {
            accountService.transfer(first, second, BigDecimal.valueOf(-123.23));
        } catch (NotValidAmountException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void transferZeroAmountTest() throws NotEnoughMoneyException {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();
        boolean exceptionThrown = false;
        try {
            accountService.transfer(first, second, BigDecimal.ZERO);
        } catch (NotValidAmountException e) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void transferAccountsEqualTest() throws NotEnoughMoneyException {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();
        boolean exceptionThrown = false;
        try {
            accountService.transfer(first, second, BigDecimal.valueOf(50l));
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void clearCacheTest() {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");

        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();

        accountService.accountsCache.put(firstAccount, first);
        accountService.accountsCache.put(secondAccount, second);

        accountService.clear();

        Assertions.assertTrue(accountService.accountsCache.isEmpty());
    }

    @Test
    public void getFromCacheTest() {
        UUID accountId = UUID.fromString("340c2cf6-cde1-4103-beba-eeb0a88db727");
        AccountServiceImpl accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(accountId), "testName", "testLastname", BigDecimal.valueOf(100l));
        accountService.accountsCache.put(accountId, account);

        Account retirevedAccount = accountService.getAccount(accountId);
        Assertions.assertEquals(account.getAccountKey(), retirevedAccount.getAccountKey());
        Assertions.assertEquals(account.getBalance(), retirevedAccount.getBalance());
        Assertions.assertEquals(account.getFirstName(), retirevedAccount.getFirstName());
        Assertions.assertEquals(account.getLastName(), retirevedAccount.getLastName());
    }

    @Test
    public void addToCacheTest() {
        UUID firstAccount = UUID.fromString("1bbff6ec-a1c5-42cf-9fc9-1c905310dfbb");
        UUID secondAccount = UUID.fromString("790ea0ce-824a-4785-be6b-e4eceb473049");

        Account first = new Account(AccountKey.valueOf(firstAccount), "testName_1", "testLastname_1", BigDecimal.valueOf(100l));
        Account second = new Account(AccountKey.valueOf(secondAccount), "testName_2", "testLastname_2", BigDecimal.valueOf(200l));
        AccountServiceImpl accountService = new AccountServiceImpl();

        accountService.createAccount(first);
        accountService.createAccount(second);

        Assertions.assertEquals(2, accountService.accountsCache.size());
        Assertions.assertTrue(accountService.accountsCache.containsKey(firstAccount));
        Assertions.assertTrue(accountService.accountsCache.containsKey(secondAccount));
        Assertions.assertEquals(accountService.accountsCache.get(firstAccount), first);
        Assertions.assertEquals(accountService.accountsCache.get(secondAccount), second);
    }

    private void processRunnable(int index,
                                Account first,
                                Account second,
                                Account third,
                                Account fourth,
                                AccountServiceImpl accountService) {
        try {
            switch (index) {
                case 0:
                    accountService.transfer(first, second, BigDecimal.valueOf(50));
                    return;
                case 1:
                    accountService.transfer(second, first, BigDecimal.valueOf(40));
                    return;
                case 2:
                    accountService.transfer(third, first, BigDecimal.valueOf(30));
                    return;
                case 3:
                    accountService.transfer(fourth, second, BigDecimal.valueOf(20));
                    return;
                case 4:
                    accountService.transfer(second, fourth, BigDecimal.valueOf(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}