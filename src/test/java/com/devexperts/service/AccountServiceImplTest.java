package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.account.Transfer;
import com.devexperts.service.dao.AccountsDB;
import com.devexperts.service.dao.TransfersDB;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import com.devexperts.service.exceptions.RecreateAccountException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {
    private AccountsDB accountsDB = mock(AccountsDB.class);
    private TransfersDB transfersDB = mock(TransfersDB.class);
    private AccountServiceImpl accountServiceImpl = new AccountServiceImpl(accountsDB, transfersDB);

    @Test
    void clear() throws RuntimeException {
        Mockito.doThrow(new RuntimeException("test")).when(accountsDB).deleteAll();
        Throwable thrown = assertThrows(RuntimeException.class, () -> accountServiceImpl.clear());
        assertEquals(thrown.getMessage(), "test");
    }

    @Test
    void createAccount() {
        System.out.println("test null as argument");
        assertThrows(IllegalArgumentException.class, () -> accountServiceImpl.createAccount(null));

        System.out.println("test if user already exists");
        Account account1 = new Account(AccountKey.valueOf(1), "name", "name", 777.0);
        when(accountsDB.existsById(account1.getAccountKey())).thenReturn(true);
        assertThrows(RecreateAccountException.class, () -> accountServiceImpl.createAccount(account1));

        System.out.println("test good save");
        Account account2 = new Account(AccountKey.valueOf(2), "name", "name", 777.0);
        Mockito.doThrow(new RuntimeException("test")).when(accountsDB).save(account2);
        Throwable thrown = assertThrows(RuntimeException.class, () -> accountServiceImpl.createAccount(account2));
        assertEquals(thrown.getMessage(), "test");
    }

    @Test
    void getAccount() throws GetAccountException {
        System.out.println("test if account not find");
        when(accountsDB.findById(AccountKey.valueOf(1))).thenReturn(Optional.empty());
        assertThrows(GetAccountException.class, () -> accountServiceImpl.getAccount(1));

        System.out.println("test account get successful");
        when(accountsDB.findById(AccountKey.valueOf(1))).thenReturn(Optional.of(new Account(AccountKey.valueOf(2), "name", "name", 777.0)));
        assertEquals(accountServiceImpl.getAccount(1), new Account(AccountKey.valueOf(2), "name", "name", 777.0));
    }

    @Test
    void transfer() {
        System.out.println("test no money");
        Account account1 = new Account(AccountKey.valueOf(1), "name", "name", 777.0);
        Account account2 = new Account(AccountKey.valueOf(2), "name", "name", 777.0);
        Throwable thrown = assertThrows(AccountsTransferAmountException.class, () -> accountServiceImpl.transfer(account1, account2, 1000));
        assertEquals(thrown.getMessage(), "transfer fail, not enough money in source account");

        System.out.println("test rollback");
        account1.setBalance(null);
        thrown = assertThrows(AccountsTransferAmountException.class, () -> accountServiceImpl.transfer(account1, account2, 1000));
        assertEquals(thrown.getMessage(), "transfer fail, need rollback");

        account1.setBalance(777.0);
        System.out.println("test good transfer");
        Mockito.doThrow(new RuntimeException("transfer")).when(transfersDB).save(any(Transfer.class));
        thrown = assertThrows(RuntimeException.class, () -> accountServiceImpl.transfer(account1, account2, 500));
        assertEquals(thrown.getMessage(), "transfer");
        assertTrue(Math.abs(account1.getBalance() - 277.0) < 0.00001, "Неверный баланс в первом аккаунте");
        assertTrue(Math.abs(account2.getBalance() - 1277.0) < 0.00001, "Неверный баланс в втором аккаунте");
    }
}