package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

public interface AccountService {

    /**
     * Clears account cache
     *
     * */
    void clear();

    /**
     * Creates a new account
     *
     * @param account account entity to add
     * @throws IllegalArgumentException if account is already present
     */
    void createAccount(@NotNull Account account);

    /**
     * Get account from the cache
     *
     * @param key identification of an account to search for
     * @return account associated with given key or {@code null} if account is not found in the cache
     */
    @Null
    Account getAccount(@NotNull AccountKey key);

    /**
     * Transfers given amount of money from source account to target account
     *
     * @param sourceKey account key to transfer money from
     * @param targetKey account key to transfer money to
     * @param amount    dollar amount to transfer
     * @return transfer result
     */
    @NotNull
    TransferResult transfer(@NotNull AccountKey sourceKey, @NotNull AccountKey targetKey, @NotNull BigDecimal amount);

}
