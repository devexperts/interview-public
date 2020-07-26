package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for checking the first task")
class AccountServiceTaskOneTest {
    private static Account account;
    private static long key = 1123456;
    private static AccountService accountService = new AccountServiceImpl();

    static {
        account = new Account(
                AccountKey.valueOf( key ), "Sergey", "Ivanov", 100d
        );
    }

    @Test
    @DisplayName("Check 'createAccount', normal work")
    void testCreateAccount() {
        accountService.clear();
        assertDoesNotThrow( () -> accountService.createAccount( account ) );
    }

    @Test
    @DisplayName("Check 'createAccount', that an 'NullPointerException' was thrown")
    void testCreateAccountThrowNullPointerException() {
        assertThrows( NullPointerException.class, () -> accountService.createAccount( null ) );
    }

    @Test
    @DisplayName("Check 'createAccount', that an 'IllegalArgumentException' was thrown")
    void testCreateAccountThrowIllegalArgumentException() {
        accountService.clear();
        accountService.createAccount( account );
        assertThrows( IllegalArgumentException.class, () -> accountService.createAccount( account ) );
    }

    @Test
    @DisplayName("Check 'getAccount', normal work")
    void testGetAccount() {
        accountService.clear();
        accountService.createAccount( account );
        assertEquals( account, accountService.getAccount( key ) );
    }

    @Test
    @DisplayName("Check 'getAccount', account is not found")
    void testGetAccountIsNotFound() {
        accountService.clear();
        assertNull( accountService.getAccount( key ) );
    }
}