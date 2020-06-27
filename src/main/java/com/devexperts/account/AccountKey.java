package com.devexperts.account;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 */
@EqualsAndHashCode
@ToString
@Embeddable
public class AccountKey implements Serializable {

    @Column(name = "id")
    private final long accountId;

    private AccountKey(long accountId) {
        this.accountId = accountId;
    }

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }
}
