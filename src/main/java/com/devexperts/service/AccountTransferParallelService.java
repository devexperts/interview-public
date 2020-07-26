package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.NegativeBalanceException;

public class AccountTransferParallelService extends AccountServiceImpl {

    private Account getAccount( AccountKey accountKey ) {
        return getAccounts().get( accountKey );
    }

    @Override
    public void transfer( Account source, Account target, double amount ) {
        checkArgumentForTransfer( source, target, amount );

        //get from the cache (to be sure)
        Account sourceCash = getAccount( source.getAccountKey() );
        Account targetCash = getAccount( target.getAccountKey() );

        if (sourceCash == null) {
            throw new IllegalArgumentException( "source-account is not found" );
        }

        if (targetCash == null) {
            throw new IllegalArgumentException( "target-account is not found" );
        }

        //used an optimistic approach to blocking resources
        synchronized ( sourceCash ) {
            if ( sourceCash.getBalance() - amount < 0 ) {
                throw new NegativeBalanceException( "source has insufficient funds on his account" );
            }

            source.setBalance( source.getBalance() - amount );
        }

        synchronized ( targetCash ) {
            target.setBalance( target.getBalance() + amount );
        }
    }
}
