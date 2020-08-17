package com.devexperts.account;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * */
@Getter
public class AccountKey {
    private final UUID accountId;

    private AccountKey(UUID accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(UUID accountId) {
        return new AccountKey(accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKey that = (AccountKey) o;
        return accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
