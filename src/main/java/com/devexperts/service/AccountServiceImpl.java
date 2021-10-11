package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientAccountBalanceException;
import org
        .springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {


    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(@NonNull Account account) {
        if (account.getAccountKey() != null) {
            if (accounts.putIfAbsent(account.getAccountKey(), account) != null) {
                throw new IllegalArgumentException("Account already exists");
            }
        } else {
            throw new IllegalArgumentException("AccountKey cannot be null");
        }
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }


    @Override
    public void transfer(Account source, Account target, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (source == null || target == null) {
            throw new AccountNotFoundException("One of the accounts was not found");
        }

        AccountKey sourceAccountKey = source.getAccountKey();
        AccountKey targetAccountKey = target.getAccountKey();


        AccountKey lock1 = sourceAccountKey.hashCode() < targetAccountKey.hashCode() ? sourceAccountKey : targetAccountKey;
        AccountKey lock2 = sourceAccountKey.hashCode() < targetAccountKey.hashCode() ? targetAccountKey : sourceAccountKey;

        synchronized (lock1) {
            synchronized (lock2) {
                Account sourceFromAccounts = accounts.get(sourceAccountKey);
                Account targetFromAccounts = accounts.get(targetAccountKey);

                if (sourceFromAccounts != null && targetFromAccounts != null) {
                    if (sourceFromAccounts.getBalance() < amount) {
                        throw new InsufficientAccountBalanceException("The source balance cannot be less than amount");
                    }
                    sourceFromAccounts.setBalance(sourceFromAccounts.getBalance() - amount);
                    targetFromAccounts.setBalance(targetFromAccounts.getBalance() + amount);
                } else {
                    throw new AccountNotFoundException("One of the accounts was not found");
                }
            }
        }
    }
}
