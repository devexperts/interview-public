package com.devexperts.entities.account;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.StringJoiner;

public class Account {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private BigDecimal balance;

    public Account(@NotNull AccountKey accountKey, @NotNull String firstName, @NotNull String lastName, @NotNull BigDecimal balance) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public boolean hasAmount(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    public void withdrawAmount(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void depositAmount(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("accountKey=" + accountKey)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("balance=" + balance)
                .toString();
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

}
