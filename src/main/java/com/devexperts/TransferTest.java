package com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private AccountService accountService;
    private Account source;
    private Account target;

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl();

        source = new Account(AccountKey.valueOf(1), "One", "First", 100.0);
        target = new Account(AccountKey.valueOf(2), "Two", "Second", 150.0);
        accountService.createAccount(source);
        accountService.createAccount(target);
    }

    @Test
    public void transfer() {
        accountService.transfer(source, target, 50.0);
        assertEquals(source.getBalance(), 50.0);
        assertEquals(target.getBalance(), 200.0);
    }
}