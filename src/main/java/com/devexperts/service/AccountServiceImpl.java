package com.devexperts.service;

import com.devexperts.account.Account;
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

        //old
//        return accounts.stream()
//                .filter(account -> account.getAccountKey() == AccountKey.valueOf(id))
//                .findAny()
//                .orElse(null);
        //new
        //AccountKey.valueOf was returning new instance of AccountKey to compare to and using == comparator
        return accounts.stream()
                .filter(account -> account.getAccountKey().getAccountId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void transfer(long sourceId, long targetId, double amount) {

        Account source = getAccount(sourceId);
        Account target = getAccount(targetId);
        transfer(source, target, amount);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {

        if (source == null || target == null) {
            throw new IllegalArgumentException();
        }
        if (source.getBalance() < amount) {
            throw new InternalError();
        }
        source.withdraw(amount);
        target.deposit(amount);
    }
}
