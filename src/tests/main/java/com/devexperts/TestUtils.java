package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

final class TestUtils {
    private TestUtils() {
        throw new IllegalAccessError("No instances!");
    }

    static Account createAccount(long id) {
        return createAccount(id, 0.0);
    }

    static Account createAccount(long id, double amount) {
        return new Account(AccountKey.valueOf(id), "FirstName", "LastName", amount);
    }
}
