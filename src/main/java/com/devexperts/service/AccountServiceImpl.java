package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account.getAccountKey() == null) {
            throw new NullPointerException();
        }
        if (accounts.containsKey(account.getAccountKey())) {
            throw new IllegalArgumentException("This key is already used.");
        }
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.getOrDefault(AccountKey.valueOf(id), null);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (amount < 0 || source == target) {
            throw new IllegalArgumentException("Amount can't be negative OR Source can't be same as target.");
        }
        double sourceBalance = source.getBalance();
        double targetBalance = target.getBalance();
        if (sourceBalance < amount)
            throw new IllegalArgumentException("Source balance is insufficient.");

        source.setBalance(sourceBalance - amount);
        target.setBalance(targetBalance + amount);
    }
}
