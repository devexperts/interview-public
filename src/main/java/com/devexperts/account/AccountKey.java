package com.devexperts.account;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * */
@Embeddable
public class AccountKey {
    @Id
    private final long accountId;

    private AccountKey(long accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountKey)) return false;
        AccountKey that = (AccountKey) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "AccountKey{" +
                "accountId=" + accountId +
                '}';
    }
}
