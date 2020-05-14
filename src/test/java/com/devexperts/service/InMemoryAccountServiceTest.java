package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.AccountAlreadyExistException;
import com.devexperts.service.exceptions.AccountNotExistException;
import com.devexperts.service.exceptions.InsufficientFundsException;
import com.devexperts.utils.ConcurrentShooter;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryAccountServiceTest {

    private final AccountService accountService = new InMemoryAccountService();

    private final long firstAccountId = 1L;
    private final long secondAccountId = 2L;
    private final AccountKey firstAccountKey = AccountKey.valueOf(firstAccountId);
    private final AccountKey secondAccountKey = AccountKey.valueOf(secondAccountId);

    @Test
    public void shouldTransferConcurrently() throws Exception {
        Account account1 = createAccount(firstAccountKey, 1_000_000.00);
        Account account2 = createAccount(secondAccountKey, 0.00);
        ConcurrentShooter concurrentShooter = new ConcurrentShooter(4, 250_000);

        concurrentShooter.shootConcurrently(() -> {
            accountService.transfer(account1, account2, 1.0);
        });

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(0, balance1);
        assertEquals(1_000_000, balance2);
    }

    @Test(timeout = 5000)
    public void shouldTransferWithoutDeadlock() throws Exception {
        Account account1 = createAccount(firstAccountKey, 1_000_000_000.00);
        Account account2 = createAccount(secondAccountKey, 1_000_000_000.00);
        ConcurrentShooter concurrentShooter = new ConcurrentShooter(4, 250_000);

        concurrentShooter.shootConcurrently(() -> {
            accountService.transfer(account1, account2, 1.00);
            accountService.transfer(account2, account1, 10.00);
        });

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(1_009_000_000, balance1);
        assertEquals(991_000_000, balance2);
    }

    @Test
    public void shouldNotTransferMoreThanAvailable() {
        Account account1 = createAccount(firstAccountKey, 1_000.00);
        Account account2 = createAccount(secondAccountKey, 0.00);

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(account1, account2, 1001d);
        });

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(1_000, balance1);
        assertEquals(0, balance2);
    }

    @Test
    public void shouldReturnErrorIfSourceAccountNotExist() {
        Account existingAccount = createAccount(firstAccountKey, 1_000.00);
        Account notExistingAccount = new Account(AccountKey.valueOf(3L), null, null, null);

        assertThrows(AccountNotExistException.class, () -> {
            accountService.transfer(notExistingAccount, existingAccount, 1001d);
        });

        Double balance = accountService.getAccount(firstAccountId).getBalance();
        assertEquals(1_000, balance);
    }

    @Test
    public void shouldReturnErrorIfTargetAccountNotExist() {
        Account existingAccount = createAccount(firstAccountKey, 1_000.00);
        Account notExistingAccount = new Account(AccountKey.valueOf(3L), null, null, null);

        assertThrows(AccountNotExistException.class, () -> {
            accountService.transfer(existingAccount, notExistingAccount, 1001d);
        });

        Double balance = accountService.getAccount(firstAccountId).getBalance();
        assertEquals(1_000, balance);
    }

    @Test
    public void shouldFailToCreateAccountTwice() {
        createAccount(firstAccountKey, 1_000.00);

        assertThrows(AccountAlreadyExistException.class, () -> {
            createAccount(firstAccountKey, 0.00);
        });

        Double balance = accountService.getAccount(firstAccountId).getBalance();
        assertEquals(1_000, balance);
    }

    private Account createAccount(AccountKey accountKey, Double balance) {
        Account account = new Account(accountKey, "Sherlock", "Holmes", balance);
        accountService.createAccount(account);
        return account;
    }

}