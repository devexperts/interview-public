package com.devexperts.service;

import com.devexperts.account.Account;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transfer implements Runnable
{
  private final Log log = LogFactory.getLog(Transfer.class);
  private final AccountService accountService;
  private final Account source;
  private final Account target;
  private final double amount;

  public Transfer(AccountService accountService, Account source, Account target, double amount) {
    this.accountService = accountService;
    this.source = source;
    this.target = target;
    this.amount = amount;
  }

  @Override
  public void run() {
    this.accountService.transfer(source, target, amount);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
