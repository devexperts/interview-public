package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.Test;

import java.math.BigDecimal;

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

  @Test
  public void testTransfer_expectOK() {
    Account accountA = new Account(AccountKey.valueOf(1), "accountA", "accountA", 100d);
    Account accountB = new Account(AccountKey.valueOf(2), "accountB", "accountB", 200d);

    AccountService accountService = new AccountServiceImpl();
    accountService.createAccount(accountA);
    accountService.createAccount(accountB);

    double amountToTransfer = 50d;
    accountService.transfer(accountA, accountB, amountToTransfer);
    assertEquals(Double.valueOf(50d), accountA.getBalance());
    assertEquals(Double.valueOf(250d), accountB.getBalance());
  }

  @Test
  public void testTransferBiggerAmount_expectNOK() {
    Account accountA = new Account(AccountKey.valueOf(1), "accountA", "accountA", 100d);
    Account accountB = new Account(AccountKey.valueOf(2), "accountB", "accountB", 200d);

    AccountService accountService = new AccountServiceImpl();
    accountService.createAccount(accountA);
    accountService.createAccount(accountB);

    double amountToTransfer = 150d;
    try {
      accountService.transfer(accountA, accountB, amountToTransfer);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(Double.valueOf(100d), accountA.getBalance());
      assertEquals(Double.valueOf(200d), accountB.getBalance());
    }
  }

  @Test
  public void testTransferInvalidAmount_expectNOK() {
    Account accountA = new Account(AccountKey.valueOf(1), "accountA", "accountA", 100d);
    Account accountB = new Account(AccountKey.valueOf(2), "accountB", "accountB", 200d);

    AccountService accountService = new AccountServiceImpl();
    accountService.createAccount(accountA);
    accountService.createAccount(accountB);

    double amountToTransfer = -10d;
    try {
      accountService.transfer(accountA, accountB, amountToTransfer);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(Double.valueOf(100d), accountA.getBalance());
      assertEquals(Double.valueOf(200d), accountB.getBalance());
    }
  }


  @Test
  public void testConcurentTransfers_expectOK() {
    Account accountA = new Account(AccountKey.valueOf(1), "accountA", "accountA", 10000d);
    Account accountB = new Account(AccountKey.valueOf(2), "accountB", "accountB", 20000d);

    Account[] accounts = {accountA, accountB};

    AccountService accountService = new AccountServiceImpl();
    accountService.createAccount(accountA);
    accountService.createAccount(accountB);

    for (int i = 0; i < 50; i++) {
      int k = i % 2;
      Thread thread = new Thread(new Transfer(accountService, accounts[k], accounts[1 - k], i));
      thread.start();
    }

    System.out.println("AccountA balance: " + accountA.getBalance());
    System.out.println("AccountB balance: " + accountB.getBalance());
    assertEquals(BigDecimal.valueOf(30000d), BigDecimal.valueOf(accountA.getBalance() + accountB.getBalance()));
  }

}
