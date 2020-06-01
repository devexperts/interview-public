package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.account.TransferResult.TransferState;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.devexperts.service.account.AccountTestUtils.createAccount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {

    private AccountCacheService cacheService;
    private AccountLockService lockService;
    private AccountService accountService;


    @Before
    public void before() {
        cacheService = new AccountCacheServiceImpl();
        lockService = new AccountLockServiceImpl(cacheService);
        accountService = new AccountServiceImpl(cacheService, lockService);
    }


    @Test
    public void testCreateGetAccount() {
        long accountId = 123L;
        AccountKey key = AccountKey.valueOf(accountId);

        assertNull(accountService.getAccount(key));

        Account newAccount = createAccount(accountId);
        accountService.createAccount(newAccount);
        Account accountFromCache = accountService.getAccount(key);

        assertSame(newAccount, accountFromCache);
    }


    @Test
    public void testCreateGetClearAccount() {
        long accountId = 123L;
        AccountKey key = AccountKey.valueOf(accountId);

        Account newAccount = createAccount(accountId);
        accountService.createAccount(newAccount);
        assertNotNull(accountService.getAccount(key));

        accountService.clear();

        assertNull(accountService.getAccount(key));
    }


    @Test
    public void testConcurrentCreateClearAccount() throws InterruptedException {
        int itCount = 1_000;
        int thCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(thCount);
        AtomicInteger accIt = new AtomicInteger();
        AtomicInteger errorIt = new AtomicInteger();

        Runnable task = () -> {
            for (int i = 0; i < itCount; i++) {
                try {
                    Account account = createAccount(accIt.getAndIncrement());
                    cacheService.cacheLockExecute(() -> {
                        accountService.createAccount(account);
                        assertNotNull(accountService.getAccount(account.getAccountKey()));
                        return null;
                    });

                    accountService.clear();
                    assertNull(accountService.getAccount(account.getAccountKey()));
                } catch (Exception e) {
                    errorIt.incrementAndGet();
                }
            }
        };

        for (int i = 0; i < thCount; i++) {
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(30L, TimeUnit.SECONDS);

        assertEquals(thCount * itCount, accIt.get());
        assertEquals(0, errorIt.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSameAccount_expectedException() {
        Account newAccount = createAccount(123L);

        accountService.createAccount(newAccount);
        accountService.createAccount(newAccount);
    }


    @Test
    public void testTransferWithWrongAcc_statusAccountNotFound() {
        Account acc1 = createAccount(1L);
        Account acc2 = createAccount(2L);

        accountService.createAccount(acc1);

        // acc1 -> acc2
        TransferResult result = accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.TEN);
        assertNotNull(result);
        assertEquals(TransferState.ACCOUNT_NOT_FOUND, result.getState());

        // acc2 -> acc1
        result = accountService.transfer(acc2.getAccountKey(), acc1.getAccountKey(), BigDecimal.TEN);
        assertNotNull(result);
        assertEquals(TransferState.ACCOUNT_NOT_FOUND, result.getState());
    }

    @Test
    public void testTransferWithWrongAmount_statusWrongAmount() {
        Account acc1 = createAccount(1L);
        Account acc2 = createAccount(2L);

        accountService.createAccount(acc1);
        accountService.createAccount(acc2);

        // amount = -10
        TransferResult result = accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.TEN.negate());
        assertNotNull(result);
        assertEquals(TransferState.WRONG_AMOUNT, result.getState());

        // amount = 0
        result = accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.ZERO);
        assertNotNull(result);
        assertEquals(TransferState.WRONG_AMOUNT, result.getState());
    }


    @Test
    public void testTransferSameAccount_statusSameAccount() {
        Account acc1 = createAccount(1L);

        accountService.createAccount(acc1);

        TransferResult result = accountService.transfer(acc1.getAccountKey(), acc1.getAccountKey(), BigDecimal.TEN);
        assertNotNull(result);
        assertEquals(TransferState.SAME_ACCOUNT, result.getState());
    }

    @Test
    public void testTransferWithInsufficientBalance_statusInsufficientBalance() {
        Account acc1 = createAccount(1L, BigDecimal.ONE);
        Account acc2 = createAccount(2L, BigDecimal.ZERO);

        accountService.createAccount(acc1);
        accountService.createAccount(acc2);

        // acc1 -> acc2 amount = 10
        TransferResult result = accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.TEN);
        assertNotNull(result);
        assertEquals(TransferState.INSUFFICIENT_BALANCE, result.getState());
    }

    @Test
    public void testTransfer_statusSuccess() {
        Account acc1 = createAccount(1L, BigDecimal.ONE);
        Account acc2 = createAccount(2L, BigDecimal.ZERO);

        accountService.createAccount(acc1);
        accountService.createAccount(acc2);

        // acc1 -> acc2 amount = 1
        TransferResult result = accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.ONE);
        assertNotNull(result);
        assertEquals(TransferState.SUCCESS, result.getState());

        assertTrue(equals(BigDecimal.ZERO, accountService.getAccount(acc1.getAccountKey()).getBalance()));
        assertTrue(equals(BigDecimal.ONE, accountService.getAccount(acc2.getAccountKey()).getBalance()));
    }


    @Test
    public void concurrentTransferTest_commonAmountShouldStayTheSame() throws InterruptedException {
        int itCount = 1_000;
        ExecutorService executor = Executors.newFixedThreadPool(5);

        Account acc1 = createAccount(1L, new BigDecimal("100"));
        Account acc2 = createAccount(2L, new BigDecimal("100"));

        accountService.createAccount(acc1);
        accountService.createAccount(acc2);

        Runnable task = () -> {
            for (int i = 0; i < itCount; i++) {
                accountService.transfer(acc1.getAccountKey(), acc2.getAccountKey(), BigDecimal.ONE);
                accountService.transfer(acc2.getAccountKey(), acc1.getAccountKey(), BigDecimal.ONE);
            }
        };

        for (int i = 0; i < 5; i++) {
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(30L, TimeUnit.SECONDS);

        assertTrue(equals(new BigDecimal("100"), accountService.getAccount(acc1.getAccountKey()).getBalance()));
        assertTrue(equals(new BigDecimal("100"), accountService.getAccount(acc2.getAccountKey()).getBalance()));
    }

    private boolean equals(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }
}
