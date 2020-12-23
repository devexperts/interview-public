package com.devexperts.account;

import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Account {
    private final AccountKey accountKey;
    
    @NotBlank
    private final String firstName;
    
    @NotBlank
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
    
    public void increaseBalance(double balance) {
    	this.balance += balance;
    }
    
    public void decreaseBalance(double balance) {
    	this.balance -= balance;
    }
}
