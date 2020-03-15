package com.devexperts.account;

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

    //Only this method is not thread safe as all other fields are immutable
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
