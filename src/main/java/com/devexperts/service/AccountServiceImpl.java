package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new TreeMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        synchronized (accounts) {
            accounts.put(account.getAccountKey(), account);
        }
    }

    @Override
    public Account getAccount(long id) {
        synchronized (accounts) {
            return accounts.getOrDefault(AccountKey.valueOf(id), null);
        }
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        synchronized (accounts) {
            if (source.getBalance() - amount < 0) {
                throw new RuntimeException("Not enough money in source account");
            }
            source.setBalance(source.getBalance() - amount);
            target.setBalance(target.getBalance() + amount);
        }
    }
}
