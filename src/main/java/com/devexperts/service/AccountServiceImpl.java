package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final List<Account> accounts = new ArrayList<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.stream()
                .filter(account -> account.getAccountKey() == AccountKey.valueOf(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
