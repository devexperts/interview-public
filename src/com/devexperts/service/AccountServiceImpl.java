package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService{

    private final List<Account> accounts = new ArrayList<>();
    private final Object monitor = new Object();

    @Override
    public void clear(){
        synchronized (monitor) {
            accounts.clear();
        }
    }

    @Override
    public void createAccount(Account account){
        synchronized (monitor) {
            accounts.add(account);
        }
    }

    @Override
    public Account getAccount(long id) {
        synchronized (monitor) {
            for (Account acc : accounts) {
                if (acc.getAccountKey().equals(AccountKey.valueOf(id))) {
                    return acc;
                }
            }
            return null;
        }
    }

    @Override
    public void transfer(Account source, Account target, double amount){
        synchronized (monitor){
            if (source.getBalance() < amount){
                //throw  new InvalidParameterException("not enough money to perform this operation");
            }
            else {
                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);
            }
        }
    }
}
