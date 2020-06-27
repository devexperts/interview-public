package com.devexperts.account;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private Double balance;
    private final AtomicBoolean depositLock = new AtomicBoolean();

    public Account(AccountKey accountKey, String firstName, String lastName, Double balance) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    /**
     * lock balance for prevent future concurrent modification problem
     *
     * @return false - balance modification forbidden, true- lock successful
     */
    public boolean lockBalance() {
        return depositLock.compareAndSet(false, true);
    }

    public boolean unLockBalance() {
        return depositLock.compareAndSet(true, false);
    }


    public AccountKey getAccountKey() {
        return accountKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
