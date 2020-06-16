package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.service.exception.AccountBalanceException;
import com.devexperts.service.exception.AccountNotFoundException;
import com.devexperts.service.exception.AccountServiceException;

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
     * */
    void createAccount(Account account);

    /**
     * Get account from the cache
     *
     * @param  id identification of an account to search for
     * @return account associated with given id or {@code null} if account is not found in the cache
     * */
    Account getAccount(long id);

    /**
     * Get account from the cache
     *
     * @param id identification of an account to search for
     * @return account associated with given id or throws AccountNotFoundException if account is
     * not found in the cache
     */
    Account getAndCheckAccount(long id) throws AccountNotFoundException;

    /**
     * Transfers given amount of money from source account to target account
     *
     * @param source account to transfer money from
     * @param target account to transfer money to
     * @param amount dollar amount to transfer
     * */
    void transfer(Account source, Account target, double amount) throws AccountBalanceException;

    /**
     * Just for database population on startup
     */
    void init();
}
