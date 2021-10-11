package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.InsufficientBalanceException;
import com.devexperts.exceptions.InvalidAmountException;
import com.devexperts.exceptions.TransferException;
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
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    private void transferLogic(Account source, Account target, double amount) {
        if(source.getAccountKey().equals(target.getAccountKey())) {
            throw new TransferException("Transfer to the same account");
        }
        double sourceAmount = source.getBalance();
        double targetAmount = target.getBalance();
        if(amount < 0) {
            throw new InvalidAmountException("Amount can not be negative");
        }
        if (sourceAmount < amount) {
            throw new InsufficientBalanceException("Insufficient account balance");
        }
        source.setBalance(sourceAmount - amount);
        target.setBalance(targetAmount + amount);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        Object mutex1 = source;
        Object mutex2 = target;
        // make sure that mutexes are accessed always in the same order to avoid deadlocks
        if (source.getAccountKey().getAccountId() > target.getAccountKey().getAccountId()) {
            mutex1 = target;
            mutex2 = source;
        }
        // account are mutexes
        // use two phase locking
        synchronized (mutex1) {
            synchronized (mutex2) {
                transferLogic(source, target, amount);
            }
        }
    }
}
