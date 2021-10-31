package com.devexperts.account;

import lombok.EqualsAndHashCode;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * */

@EqualsAndHashCode
public class AccountKey {
    private final long accountId;

    private AccountKey(long accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }

    public long getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "AccountKey{accountId=" + accountId + '}';
    }
}
