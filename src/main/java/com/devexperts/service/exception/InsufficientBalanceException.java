package com.devexperts.service.exception;

import com.devexperts.account.Account;

/**
 * @author pashkevich.ea
 */
public class InsufficientBalanceException extends RuntimeException {

    private final Account account;
    private final double amount;

    public InsufficientBalanceException(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }
}
