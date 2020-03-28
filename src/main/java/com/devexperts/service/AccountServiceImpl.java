package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  private final Map<Long, Account> accounts;

  public AccountServiceImpl() {
    accounts = new HashMap<>();
  }

  @Override
  public void clear() {
    accounts.clear();
  }

  @Override
  public void createAccount(Account account) {
    accounts.put(account.getAccountKeyId(), account);
  }

  @Override
  public Account getAccount(long id) {
    return accounts.get(id);
  }

  @Override
  public void transfer(Account source, Account target, double amount) {
    // In order to avoid a deadlock we acquire locks in the same order always.
    final Account lock1 =
        source.getAccountKeyId() < target.getAccountKeyId() ? source : target;
    final Account lock2 =
        source.getAccountKeyId() < target.getAccountKeyId() ? target : source;
    synchronized (lock1) {
      synchronized (lock2) {
        source.withdraw(amount);
        target.deposit(amount);
      }
    }
  }
}
