package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountServiceImplTest {
    private AccountService accountService;
    private Account account;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();

        account = new Account(AccountKey.valueOf(1), "IVAN", "IVANOV", new BigDecimal("100.0"));
        accountService.createAccount(account);
    }

    /* AccountServiceImp.clear() */
    @Test
    public void shouldReturnNull_ifCollectionIsEmpty() {
        Account expected = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNotNull(expected);

        accountService.clear();
        expected = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNull(expected);
    }

    /* AccountServiceImp.createAccount() */
    @Test
    public void shouldReturnTrue_ifNewAccountCreated() {
        Account expected = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertEquals(account,expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_ifAccountIsNull() {
        accountService.createAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_ifAccountAlreadyExists() {
        accountService.createAccount(account);
    }

    /* AccountServiceImp.getAccount() */
    @Test
    public void shouldReturnTrue_ifAccountExists() {
        Account expected = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNotNull(expected);
    }

    @Test
    public void shouldReturnNull_ifAccountNotFound() {
        Account expected = accountService.getAccount(2);
        Assert.assertNull(expected);
    }
}
