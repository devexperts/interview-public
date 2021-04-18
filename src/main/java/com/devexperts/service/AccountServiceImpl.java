package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        Account prev = accounts.get(account.getAccountKey());
        if (prev != null) {
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(account.getAccountKey(), account);
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
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
    }
}
