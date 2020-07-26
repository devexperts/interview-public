package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    //Changed the collection, because the speed of searching for an element in HashMap is constant.
    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    /**
     * @throws NullPointerException if account null
     */
    @Override
    public void createAccount( Account account ) throws IllegalArgumentException, NullPointerException {
        if ( account == null ) {
            throw new NullPointerException( "account is null" );
        }

        if ( accounts.containsKey( account.getAccountKey() ) ) {
            throw new IllegalArgumentException( "account is already present" );
        }

        accounts.put( account.getAccountKey(), account );
    }

    @Override
    public Account getAccount( long id ) {
        return accounts.get( AccountKey.valueOf( id ) );
    }

    @Override
    public void transfer( Account source, Account target, double amount ) {
        //do nothing for now
    }
}
