package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    private Account account;
    private AccountServiceImpl service = new AccountServiceImpl();

    @BeforeEach
    void setUp() {
        service = new AccountServiceImpl();
        AccountKey key = AccountKey.valueOf(100);
        account = new Account(key, "name", "lastname", 0d);
    }

    @Test
    void test_NewAccountCreation() {
        service.createAccount(account);
        Account actual = service.getAccount(account.getAccountKey().getAccountId());

        assertEquals(account.getAccountKey(), actual.getAccountKey());
        assertEquals(account, actual);
    }

    @Test
    void test_CreateOneKeyAccounts() {
        service.createAccount(account);

        Account newAccount = account;
        assertThrows(
                IllegalArgumentException.class, () -> service.createAccount(newAccount)
        );
    }

    @Test
    void test_CreateNullKeyAccounts() {
        account = new Account(null, "name", "lastname", 0d);
        assertThrows(
                NullPointerException.class, () -> service.createAccount(account)
        );
    }

    @Test
    void test_CreateAccounts() {
        AccountKey keyOne = AccountKey.valueOf(1);
        account = new Account(keyOne, "name1", "lastname1", 0d);
        service.createAccount(account);

        AccountKey keyTwo = AccountKey.valueOf(2);
        account = new Account(keyTwo, "name2", "lastname2", 0d);
        service.createAccount(account);

        assertNotEquals(service.getAccount(keyOne.getAccountId()), service.getAccount(keyTwo.getAccountId()));
    }

    @Test
    void test_ClearAccountsMap() {
        service.createAccount(account);
        Account actual = service.getAccount(account.getAccountKey().getAccountId());
        assertNotNull(actual);
        service.clear();
        actual = service.getAccount(account.getAccountKey().getAccountId());
        assertNull(actual);
    }
}