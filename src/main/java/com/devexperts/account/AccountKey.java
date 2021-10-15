package com.devexperts.account;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * */
public class AccountKey {
    private final long accountId;

    protected AccountKey(long accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }

    @Override
    public boolean equals(Object anotherKey) {
        if (anotherKey instanceof AccountKey) {
            return this.accountId == ((AccountKey) anotherKey).accountId;
        } else
        {
            return false;
        }
    }

    public int compare(AccountKey anotherKey) {
        return Long.compare(this.accountId, anotherKey.accountId);
    }

}
