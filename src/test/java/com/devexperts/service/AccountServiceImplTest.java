package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class AccountServiceImplTest {
    private AccountServiceImpl accountService = new AccountServiceImpl();

    @BeforeEach
    void init() {
        accountService.createAccount(new Account(AccountKey.valueOf(1L), "Ivan", "Ivanov", 500.0));
        accountService.createAccount(new Account(AccountKey.valueOf(2L), "Petr", "Petrov", 350.0));
        accountService.createAccount(new Account(AccountKey.valueOf(3L), "Igor", "Sidorov", 750.0));
    }

    @org.junit.jupiter.api.Test
    void getAccountFromAccounts() {
        Account account = accountService.getAccount(1L);
        Assert.assertNotNull(account);
        Assert.assertEquals("Ivan", account.getFirstName());
        Assert.assertEquals("Ivanov", account.getLastName());
        Assert.assertEquals(500.0, account.getBalance(), 0.00001);
    }

    @org.junit.jupiter.api.Test
    void getAccountNotFromAccounts() {
        Account account = accountService.getAccount(5L);
        Assert.assertNull(account);
    }

    @AfterEach
    void tearDown() {
        accountService.clear();
    }
}