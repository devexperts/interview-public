package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;

import java.math.BigDecimal;

public class AccountTestUtils {

    public static Account createAccount(long id) {
        return createAccount(id, BigDecimal.ZERO);
    }

    public static Account createAccount(long id, BigDecimal balance) {
        return new Account(
                AccountKey.valueOf(id),
                "firstName-" + id,
                "lastName-" + id,
                balance
        );
    }
}
