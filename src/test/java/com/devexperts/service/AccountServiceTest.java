package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {

  @Test
  public void testAddAccount() {
    Account accountA = new Account(AccountKey.valueOf(1), "accountA", "accountA", 100d);
    Account accountB = new Account(AccountKey.valueOf(2), "accountB", "accountB", 200d);

    AccountService accountService = new AccountServiceImpl();
    accountService.createAccount(accountA);
    accountService.createAccount(accountB);

    assertEquals(accountA, accountService.getAccount(1));
    assertEquals(accountB, accountService.getAccount(2));
    assertNull(accountService.getAccount(3));
  }
}
