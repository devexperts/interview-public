package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exception.InvalidAmountException;
import com.devexperts.service.exception.NotFoundAccountException;
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
                .filter(account -> account.getAccountKey().equals(AccountKey.valueOf(id)))
                .findAny()
                .orElseThrow(() -> new NotFoundAccountException(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (source.getAccountKey().compareTo(target.getAccountKey()) >= 0) {
            synchronized (source) {
                synchronized (target) {
                    doTransfer(source, target, amount);
                }
            }
        } else {
            synchronized (target) {
                synchronized (source) {
                    doTransfer(source, target, amount);
                }
            }
        }
    }

    private void doTransfer(Account source, Account target, double amount) {
        if (!accounts.contains(source)) {
            throw new NotFoundAccountException(source);
        }
        if (!accounts.contains(target)) {
            throw new NotFoundAccountException(target);
        }
        if (source.getBalance() < amount) {
            throw new InvalidAmountException();
        }
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
    }
}
