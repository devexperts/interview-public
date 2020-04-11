package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.AccountNotFountException;
import com.devexperts.exception.InsufficientAccountBalanceException;
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
    public void transfer(Long sourceAccountId, Long targetAccountId, double amount) {
        synchronized (accounts) {
            Account source = accounts.get(AccountKey.valueOf(sourceAccountId));
            if (source == null) {
                throw new AccountNotFountException("Source account with id " + sourceAccountId + "does not exist");
            }
            Account target = accounts.get(AccountKey.valueOf(targetAccountId));
            if (target == null) {
                throw new AccountNotFountException("Target account with id " + targetAccountId + "does not exist");
            }
            if (source.getBalance() - amount < 0) {
                throw new InsufficientAccountBalanceException("Not enough money in source account with id" + sourceAccountId);
            }
            source.setBalance(source.getBalance() - amount);
            target.setBalance(target.getBalance() + amount);
        }
    }
}
