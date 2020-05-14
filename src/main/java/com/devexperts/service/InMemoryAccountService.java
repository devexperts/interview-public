package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.AccountAlreadyExistException;
import com.devexperts.service.exceptions.AccountNotExistException;
import com.devexperts.service.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryAccountService implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account.getAccountKey() == null) {
            throw new IllegalArgumentException();
        }
        Account existingAccount = accounts.putIfAbsent(account.getAccountKey(), account);
        if (existingAccount != null) {
            throw new AccountAlreadyExistException();
        }
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        validateAccounts(source, target, amount);

        AccountKey firstMonitor = source.getAccountKey();
        AccountKey secondMonitor = target.getAccountKey();
        if (firstMonitor.compareTo(secondMonitor) < 0) {
            firstMonitor = target.getAccountKey();
            secondMonitor = source.getAccountKey();
        }
        synchronized (firstMonitor) {
            synchronized (secondMonitor) {
                source = accounts.get(source.getAccountKey());
                target = accounts.get(target.getAccountKey());

                validateAccounts(source, target, amount);

                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);
            }
        }
    }

    private void validateAccounts(Account source, Account target, double amount) {
        if (source == null
                || target == null
                || !accounts.containsKey(source.getAccountKey())
                || !accounts.containsKey(target.getAccountKey())) {
            throw new AccountNotExistException();
        }
        if (source.getBalance() < amount) {
            throw new InsufficientFundsException();
        }
    }
}
