package com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultithreadTransferTest {
    AccountServiceImpl service;
    Account source, target;
    AccountKey skey, tkey;

    @BeforeEach
    private void setUp() {
        service = new AccountServiceImpl();
        skey = AccountKey.valueOf(1);
        source = new Account(skey, "One", "First", 100.0);

        tkey = AccountKey.valueOf(2);
        target = new Account(tkey, "Two", "Second", 200.0);
    }

    @Test
    void multithreadTransfer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                service.transfer(source, target, 10.0);
                latch.countDown();
            }).start();
        }
        latch.await(1, TimeUnit.SECONDS);

        assertEquals(0.0, source.getBalance());
        assertEquals(300.0, target.getBalance());
    }
}
