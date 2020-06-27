package com.devexperts.account;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Entity
@Table(name = "accounts")
public class Account {
    @EmbeddedId
    private AccountKey accountKey;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "BALANCE")
    private Double balance; //BigDecimal for raise accuracy
    private final AtomicBoolean depositLock = new AtomicBoolean();

    public Account() {
    }

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
