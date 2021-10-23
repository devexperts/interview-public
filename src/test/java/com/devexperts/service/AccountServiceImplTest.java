package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    AccountService accountService;

    @Test
    void transfer() {
        Account ac1 = new Account(AccountKey.valueOf(1L), "Abdusame", "Ochil-zoda", 100.00);
        Account ac2 = new Account(AccountKey.valueOf(1L), "Alex", "Ivanov", 33.00);
        accountService.transfer(ac1, ac2, 19.22);

        assertEquals(100.00 - 19.22, ac1.getBalance());
        assertEquals(33.00 + 19.22, ac2.getBalance());
    }
}