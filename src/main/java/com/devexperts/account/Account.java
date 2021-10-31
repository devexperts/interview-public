package com.devexperts.account;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Builder(builderMethodName = "of")
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

    public void increaseBalance(double amount) {
        this.balance += amount;
    }

    public void decreaseBalance(double amount){
        this.balance -= amount;
    }

    public long getAccountId(){
        return accountKey.getAccountId();
    }

    @Override
    public String toString() {
        return accountKey.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return new EqualsBuilder()
                .append(getAccountKey(), account.getAccountKey())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAccountKey())
                .toHashCode();
    }
}
