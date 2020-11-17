package com.devexperts.account;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest{

  @Test
  public void testAccountWithdraw_expectOK() {
    Account account = new Account(AccountKey.valueOf(1), "account", "account", 100d);
    account.withdraw(10d);
    Assert.assertEquals(Double.valueOf(90d), account.getBalance());
  }

  @Test
  public void testAccountWithdraw_insufficient_expectNOK() {
    Account account = new Account(AccountKey.valueOf(1), "account", "account", 100d);
    try {
      account.withdraw(110d);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Double.valueOf(100d), account.getBalance());
    }
  }

  @Test
  public void testAccountWithdraw_expectNOK() {
    Account account = new Account(AccountKey.valueOf(1), "account", "account", 100d);
    try {
      account.withdraw(-10d);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Double.valueOf(100d), account.getBalance());
    }
  }

  @Test
  public void testAccountDeposit_expectOK() {
    Account account = new Account(AccountKey.valueOf(1), "account", "account", 100d);
    account.deposit(10d);
    Assert.assertEquals(Double.valueOf(110d), account.getBalance());
  }

  @Test
  public void testAccountDeposit_expectNOK() {
    Account account = new Account(AccountKey.valueOf(1), "account", "account", 100d);
    try {
      account.deposit(-10d);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Double.valueOf(100d), account.getBalance());
    }
  }
}
