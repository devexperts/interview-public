package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.InsufficientBalanceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ValidateTransferParamsTest {
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

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_ifSourceIsNull() {
        accountService.validateTransferParams(null,target,new BigDecimal("50.0"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_ifTargetIsNull() {
        accountService.validateTransferParams(source,null,new BigDecimal("50.0"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_ifSourceNotExists() {
        source = new Account(AccountKey.valueOf(3), "MARK", "MARKOV", new BigDecimal("10.0"));
        accountService.validateTransferParams(source,target,new BigDecimal("50.0"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_ifTargetNotExists() {
        source = new Account(AccountKey.valueOf(3), "MARK", "MARKOV", new BigDecimal("10.0"));
        accountService.validateTransferParams(source,target,new BigDecimal("50.0"));
    }

    @Test
    public void shouldReturnTrue_ifAccountsEqual() {
        Assert.assertNotEquals(source,target);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowIllegalArgumentException_ifSourceBalanceIsInsufficient_1() {
        source.setBalance(BigDecimal.ZERO);
        accountService.validateTransferParams(source,target,new BigDecimal("50.0"));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowIllegalArgumentException_ifSourceBalanceIsInsufficient_2() {
        accountService.validateTransferParams(source,target,new BigDecimal("150.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_ifAmountLessThanZero () {
        accountService.validateTransferParams(source,target,new BigDecimal("-50.0"));
    }
}
