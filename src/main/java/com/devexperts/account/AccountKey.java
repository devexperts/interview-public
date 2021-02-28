package com.devexperts.account;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 */
public class AccountKey implements Comparable<AccountKey> {
    private final long accountId;

    private AccountKey(long accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccountKey))
            return false;
        AccountKey accountKey = (AccountKey) obj;
        return accountId == accountKey.accountId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(accountId);
    }

    @Override
    public int compareTo(AccountKey accountKey) {
        return Long.compare(accountId, accountKey.accountId);
    }
}
