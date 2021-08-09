package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferTest {
    private AccountService accountService;
    private Account source;
    private Account target;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();

        source = new Account(AccountKey.valueOf(1), "IVAN", "IVANOV", new BigDecimal("100.0"));
        target = new Account(AccountKey.valueOf(2), "PETER", "PETROV", new BigDecimal("200.0"));
        accountService.createAccount(source);
        accountService.createAccount(target);
    }

    // single-thread environment
    @Test
    public void shouldReturnTrue_ifSingleThreadTransferIsPossible() {
        accountService.transfer(source, target, new BigDecimal("50.0"));
        Assert.assertEquals(source.getBalance(), new BigDecimal("50.0"));
        Assert.assertEquals(target.getBalance(), new BigDecimal("250.0"));
    }
}
