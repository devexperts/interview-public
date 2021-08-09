package com.devexperts.account;

import java.math.BigDecimal;

public class Account {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private BigDecimal balance;

    public Account(AccountKey accountKey, String firstName, String lastName, BigDecimal balance) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
