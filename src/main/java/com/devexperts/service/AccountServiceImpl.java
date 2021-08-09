package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    // change List to Map to improve the performance of the getAccount() method
    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account == null) {
            throw new NullPointerException("ACCOUNT EQUALS TO NULL.");
        }
        if (accounts.containsKey(account.getAccountKey())) {
            throw new IllegalArgumentException("ACCOUNT WITH SUCH A KEY ALREADY EXISTS.");
        }
        accounts.put(account.getAccountKey(),account);
    }

    @Override
    public Account getAccount(long id) {
        if (accounts.containsKey(AccountKey.valueOf(id))) {
            return accounts.get(AccountKey.valueOf(id));
        }
        return null;
    }
    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
