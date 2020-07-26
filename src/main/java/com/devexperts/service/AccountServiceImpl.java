package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.NegativeBalanceException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public void createAccount( Account account ) {
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
        checkArgumentForTransfer( source, target, amount );
        transferAmount( source, target, amount );
    }

    protected void checkArgumentForTransfer( Account source, Account target, double amount ) {
        if ( amount <= 0 ) {
            throw new IllegalArgumentException( "amount is negative or zero" );
        }

        if ( source == null || target == null ) {
            throw new IllegalArgumentException( "source or target is null" );
        }

        if ( source.getAccountKey().equals( target.getAccountKey() ) ) {
            throw new IllegalArgumentException( "source and target is one account" );
        }
    }

    private void transferAmount( Account source, Account target, double amount ) {
        if ( source.getBalance() - amount < 0 ) {
            throw new NegativeBalanceException( "source has insufficient funds on his account" );
        }
        source.setBalance( source.getBalance() - amount );
        target.setBalance( target.getBalance() + amount );
    }

    protected Map<AccountKey, Account> getAccounts() {
        return accounts;
    }
}
