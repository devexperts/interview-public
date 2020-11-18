package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    private final Log log = LogFactory.getLog(AccountServiceImpl.class);

    private Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        AccountKey accountKey = account.getAccountKey();
        if (accounts.containsKey(accountKey)) {
            throw new IllegalArgumentException(String.format("Account with key %s already exists!", accountKey));
        }
        accounts.put(accountKey, account);
        log.debug(String.format("Account with key %s was created!", account.getAccountKey()));
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasAccount(Account account) {
        return accounts.containsKey(account.getAccountKey());
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (source == null || target == null || source.equals(target)) {
            throw new IllegalArgumentException("Invalid source/target account!");
        }

        log.debug(String.format("Going to transfer $%s from %s to %s", amount, source.getLastName(), target.getLastName()));

        if (!hasAccount(source)) {
            createAccount(source);
        }
        if (!hasAccount(target)) {
            createAccount(target);
        }

        synchronized (this) {
            source.withdraw(amount);
            target.deposit(amount);
        }
        log.debug(String.format("Amount left in %s is $%s", source.getLastName(), source.getBalance()));
    }
}
