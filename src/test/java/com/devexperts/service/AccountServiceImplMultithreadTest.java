package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountServiceImplMultithreadTest {
    @Test
    void test_MultithreadAccountsTransaction() throws InterruptedException {
        AccountServiceImpl service = new AccountServiceImpl();

        AccountKey key = AccountKey.valueOf(1);
        Account source = new Account(key, "name", "lastname", 100.0);
        key = AccountKey.valueOf(2);
        Account target = new Account(key, "name", "lastname", 100.0);

        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                service.transfer(source, target, 10.0);
                latch.countDown();
            }).start();
        }
        latch.await(1, TimeUnit.SECONDS);

        assertEquals(0.0, source.getBalance());
        assertEquals(200.0, target.getBalance());
    }
}
