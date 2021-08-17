package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final HashMap<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account.getAccountKey() == null)
            throw new NullPointerException();
        if (accounts.get(account.getAccountKey()) != null)
            throw new IllegalArgumentException("Accounts map contain this key");
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.getOrDefault(AccountKey.valueOf(id), null);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
