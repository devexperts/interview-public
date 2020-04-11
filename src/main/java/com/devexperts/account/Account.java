package com.devexperts.account;

import java.util.Objects;

public class Account implements Comparable<Account> {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;

    private Double balance;

    public Account(AccountKey accountKey, String firstName, String lastName) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = Double.valueOf("0");
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountKey, account.accountKey) &&
                Objects.equals(firstName, account.firstName) &&
                Objects.equals(lastName, account.lastName) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountKey, firstName, lastName, balance);
    }

    @Override
    public int compareTo(Account account) {
        int accountIdDiff = this.accountKey.compareTo(account.getAccountKey());
        if (accountIdDiff != 0) {
            return accountIdDiff;
        }
        int firstNameDiff = this.firstName.compareTo(account.getFirstName());
        if (firstNameDiff != 0) {
            return firstNameDiff;
        }
        int lastNameDiff = this.lastName.compareTo(account.getLastName());
        if (lastNameDiff != 0) {
            return lastNameDiff;
        }
        int balanceDiff = this.balance.compareTo(account.getBalance());
        if (balanceDiff != 0) {
            return balanceDiff;
        }
        return 0;
    }
}
