package com.devexperts.account;

import com.devexperts.account.exceptions.InsufficientFundsException;

@SuppressWarnings("unused")
public class Account {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private Double balance;

    public Account(AccountKey accountKey, String firstName, String lastName, Double balance) {
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void withdraw(Double amount) {
        if (amount == null || amount.compareTo(0d) < 0) {
            throw new IllegalArgumentException("Invalid amount to withdraw with!");
        }

        if (amount.compareTo(this.balance) > 0) {
            throw new InsufficientFundsException();
        }

        this.balance -= amount;
    }

    public void deposit(Double amount) {
        if (amount == null || amount.compareTo(0d) < 0) {
            throw new IllegalArgumentException("Invalid amount to deposit");
        }

        this.balance += amount;
    }

    @Override
    public String toString() {
        return "Account{" +
          "accountKey=" + accountKey +
          ", firstName='" + firstName + '\'' +
          ", lastName='" + lastName + '\'' +
          ", balance=" + balance +
          '}';
    }
}
