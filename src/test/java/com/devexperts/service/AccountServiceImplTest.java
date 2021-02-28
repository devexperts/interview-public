package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exception.InvalidAmountException;
import com.devexperts.service.exception.NotFoundAccountException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

class AccountServiceImplTest {
    private static final double DELTA = 0.00001;

    private AccountServiceImpl accountService = new AccountServiceImpl();
    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    void init() {
        account1 = new Account(AccountKey.valueOf(1L), "Ivan", "Ivanov", 500.0);
        account2 = new Account(AccountKey.valueOf(2L), "Petr", "Petrov", 350.0);
        account3 = new Account(AccountKey.valueOf(3L), "Igor", "Sidorov", 750.0);
        accountService.createAccount(account1);
        accountService.createAccount(account2);
        accountService.createAccount(account3);
    }

    @org.junit.jupiter.api.Test
    void getAccountFromAccounts() {
        Account account = accountService.getAccount(1L);
        Assert.assertNotNull(account);
        Assert.assertEquals("Ivan", account.getFirstName());
        Assert.assertEquals("Ivanov", account.getLastName());
        Assert.assertEquals(500.0, account.getBalance(), DELTA);
    }

    @org.junit.jupiter.api.Test
    void getAccountNotFromAccounts() {
        Account account = accountService.getAccount(5L);
        Assert.assertNull(account);
    }

    @org.junit.jupiter.api.Test
    void validTransfer() {
        accountService.transfer(account1, account2, 200);
        Assert.assertEquals(300, account1.getBalance(), DELTA);
        Assert.assertEquals(550, account2.getBalance(), DELTA);
    }

    @org.junit.jupiter.api.Test
    void invalidAmountTransfer() {
        Assertions.assertThrows(InvalidAmountException.class, () -> {
            accountService.transfer(account2, account3, 500);
        });
    }

    @org.junit.jupiter.api.Test
    void transferFromAccountNotFromAccounts() {
        Assertions.assertThrows(NotFoundAccountException.class, () -> {
            accountService.transfer(new Account(AccountKey.valueOf(8L), "NoName", "NoSurname", 1500.0), account3, 500);
        });
    }

    @AfterEach
    void tearDown() {
        accountService.clear();
    }
}