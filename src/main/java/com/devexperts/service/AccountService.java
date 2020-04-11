package com.devexperts.service;

import com.devexperts.account.Account;

public interface AccountService {

    /**
     * Clears account cache
     */
    void clear();

    /**
     * Creates a new account
     *
     * @param account account entity to add or update
     * @throws IllegalArgumentException if account is already present
     */
    void createAccount(Account account);

    /**
     * Get account from the cache
     *
     * @param id identification of an account to search for
     * @return account associated with given id or {@code null} if account is not found in the cache
     */
    Account getAccount(long id);

    /**
     * Transfers given amount of money from source account to target account
     *
     * @param sourceAccountId account key to transfer money from
     * @param targetAccountId account key to transfer money to
     * @param amount          dollar amount to transfer
     */
    void transfer(Long sourceAccountId, Long targetAccountId, double amount);
}
