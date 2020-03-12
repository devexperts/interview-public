package com.devexperts.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accountsPer = new HashMap<AccountKey, Account>();
    
    @Override
    public void clear() {
        accountsPer.clear();
    }

    @Override
    public void createAccount(Account account) {
        accountsPer.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
    	return accountsPer.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
