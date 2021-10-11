package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class AccountServiceImplTest {

    private AccountServiceImpl accountService;


    @BeforeEach
    void initAccountServiceImpl() {
        this.accountService = new AccountServiceImpl();
    }

    @Test
    void transferInMultiThreadsThenTransferSuccessful() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        accountService.createAccount(new Account(AccountKey.valueOf(1), "1", "2", (double) 0));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "1", "2", (double) 1000000));


        final Account account1 = accountService.getAccount(1);
        final Account account2 = accountService.getAccount(2);

        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    for (int i = 0; i < 100000; i++) {
                        accountService.transfer(account2, account1, (double) 10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    for (int i = 0; i < 1000000; i++) {
                        accountService.transfer(account2, account1, 1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    Thread.sleep(1005);
                    for (int i = 0; i < 1000; i++) {
                        accountService.transfer(account1, account1, 1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        final Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    Thread.sleep(1005);
                    for (int i = 0; i < 1000; i++) accountService.transfer(account1, account2, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        latch.countDown();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        Assertions.assertEquals(1000000, account1.getBalance() + account2.getBalance());
    }
}
