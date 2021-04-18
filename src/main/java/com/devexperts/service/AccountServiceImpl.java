package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();
    private final Object transferLocker = new Object();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        Account prev = accounts.putIfAbsent(account.getAccountKey(), account);
        if (prev != null) {
            throw new IllegalArgumentException("Account already exists.");
        }
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("A negative amount is not allowed.");
        }
        synchronized (transferLocker) {
            if (source.getBalance() == null) {
                throw new IllegalStateException(
                        String.format("Transfer unavailable for source account with id: %d",
                                source.getAccountKey().getAccountId()));
            }
            if (target.getBalance() == null) {
                throw new IllegalStateException(
                        String.format("Transfer unavailable for target account with id: %d",
                                source.getAccountKey().getAccountId()));
            }
            if (source.getBalance() < amount) {
                throw new IllegalStateException("Insufficient funds on the account.");
            }
            final double sourceNewBalance = source.getBalance() - amount;
            final double targetNewBalance = target.getBalance() + amount;
            source.setBalance(sourceNewBalance);
            target.setBalance(targetNewBalance);
        }
    }
}
