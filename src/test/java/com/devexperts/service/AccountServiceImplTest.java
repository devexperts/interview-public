package com.devexperts.service;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class AccountServiceImplTest {

    AccountServiceImpl accountService;

    @BeforeEach
    void clearAccountService () {
        accountService = new AccountServiceImpl();
    }

    @Test
    void testCreateAccountAndGetAccountById() {
        Account testAccount = new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0);
        accountService.createAccount(testAccount);
        Assertions.assertEquals(testAccount, accountService.getAccount(1));
    }

    @Test
    void testCreateAccountAndGetAccountByAccountKey() {
        Account testAccount = new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0);
        accountService.createAccount(testAccount);
        Assertions.assertEquals(testAccount, accountService.getAccount(AccountKey.valueOf(1)));
    }

    @Test
    void testCreateAccountThatAlreadyExisted() {
        Account testAccount = new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0);
        accountService.createAccount(testAccount);
        Assertions.assertEquals(1, accountService.createAccount(testAccount));
    }

    @Test
    void testGetNonExistingAccountById() {
        Assertions.assertNull(accountService.getAccount(1));
    }

    @Test
    void testGetNonExistingAccountByAccountKey() {
        Assertions.assertNull(accountService.getAccount(AccountKey.valueOf(1)));
    }

    @Test
    void testGetAccountFromServiceWithManyAccounts() {
        Account testAccount = new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0);
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Petr", "Petrov", 50.0));
        accountService.createAccount(testAccount);
        accountService.createAccount(new Account(AccountKey.valueOf(3), "Ivan", "Ivanov", 0.0));
        Assertions.assertEquals(testAccount, accountService.getAccount(testAccount.getAccountKey()));
    }

    @Test
    void testClear() {
        Account testAccount = new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0);
        accountService.createAccount(testAccount);
        Assertions.assertNotNull(accountService.getAccount(1));
        accountService.clear();
        Assertions.assertNull(accountService.getAccount(1));
    }
}