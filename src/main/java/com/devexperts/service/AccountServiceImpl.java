package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.AccountDoesNotExistExceptions;
import com.devexperts.exceptions.InsufficientBalanceException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey,Account> accounts = new HashMap();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if(account == null){
            throw new NullPointerException();
        }
        if(accounts.containsKey(account.getAccountKey())){
            throw new IllegalArgumentException("Account exist");
        }

        accounts.put(account.getAccountKey(),account);
    }

    @Override
    public Account getAccount(long id) {
        return  accounts.containsKey(AccountKey.valueOf(id)) ? accounts.get(AccountKey.valueOf(id)) : null;
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (source == null || target == null) {
            throw new AccountDoesNotExistExceptions("no data entered");
        }

        if (!accounts.containsKey(source.getAccountKey())) {
            throw new AccountDoesNotExistExceptions("the sender's account does not exist");
        }

        if (!accounts.containsKey(target.getAccountKey())) {
            throw new AccountDoesNotExistExceptions("recipient account does not exist");
        }

        if (amount > source.getBalance()) {
            throw new InsufficientBalanceException("Balance is low");
        }

        Object lock1 = source.getAccountKey().getAccountId() < target.getAccountKey().getAccountId() ? source : target;
        Object lock2 = source.getAccountKey().getAccountId() < target.getAccountKey().getAccountId() ? target : source;

        synchronized (lock1) {
            synchronized (lock2) {
                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);
            }
        }
    }
}
