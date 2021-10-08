package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
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
        //do nothing for now
    }
}
