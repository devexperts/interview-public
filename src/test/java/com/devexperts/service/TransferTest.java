package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TransferTest {
    private AccountService accountService;
    private Account source;
    private Account target;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();

        source = new Account(AccountKey.valueOf(1), "IVAN", "IVANOV", new BigDecimal("100.0"));
        target = new Account(AccountKey.valueOf(2), "PETER", "PETROV", new BigDecimal("200.0"));
        accountService.createAccount(source);
        accountService.createAccount(target);
    }

    // single-thread environment
    @Test
    public void shouldReturnTrue_ifSingleThreadTransferIsPossible() {
        accountService.transfer(source, target, new BigDecimal("50.0"));
        Assert.assertEquals(source.getBalance(), new BigDecimal("50.0"));
        Assert.assertEquals(target.getBalance(), new BigDecimal("250.0"));
    }

    // multi-threads environment
    @Test
    public void shouldReturnTrue_ifPossibleToCreateAccountsFromDifferentThreads() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        // start from id=3 since accounts with id=1 and id=2 have been already created in the setUp() method
        for (int i = 3; i < 10; i++) {
            int id = i;
            new Thread(() -> {
                Account account = new Account(AccountKey.valueOf(id), "IVAN", "IVANOV", BigDecimal.ZERO);
                accountService.createAccount(account);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await(1, TimeUnit.SECONDS);

        for (int i = 3; i < 10; i++) {
            Account account = accountService.getAccount(i);
            Assert.assertNotNull(account);
            Assert.assertEquals(i, account.getAccountKey().getAccountId());
        }
    }

    @Test
    public void shouldReturnTrue_ifMultiThreadsTransferIsPossible() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Runnable transaction = () -> {
            accountService.transfer(source,target,new BigDecimal("10.0"));
            countDownLatch.countDown();
        };
        for (int i = 0; i < 10; i++) {
            new Thread(transaction).start();
        }
        countDownLatch.await(1, TimeUnit.SECONDS);

        Assert.assertEquals(source.getBalance(),BigDecimal.ZERO);
        Assert.assertEquals(target.getBalance(),new BigDecimal("300.0"));
    }
}
