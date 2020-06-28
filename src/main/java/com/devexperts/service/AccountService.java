package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import com.devexperts.service.exceptions.RecreateAccountException;

public interface AccountService {

    /**
     * Clears account cache
     *
     * */
    void clear();

    /**
     * Creates a new account
     *
     * @param account account entity to add or update
     * @throws IllegalArgumentException if account is already present
     */
    void createAccount(Account account) throws RecreateAccountException;

    /**
     * Get account from the cache
     *
     * @param  id identification of an account to search for
     * @return account associated with given id or {@code null} if account is not found in the cache
     * */
    Account getAccount(long id) throws GetAccountException;

    /**
     * Transfers given amount of money from source account to target account
     *
     * @param source account to transfer money from
     * @param target account to transfer money to
     * @param amount dollar amount to transfer
     * */
    void transfer(Account source, Account target, double amount) throws AccountsTransferAmountException;
}
