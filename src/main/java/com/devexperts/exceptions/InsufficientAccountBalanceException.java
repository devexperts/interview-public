package com.devexperts.exceptions;

public class InsufficientAccountBalanceException  extends Exception {
    public InsufficientAccountBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientAccountBalanceException(String message) {
        super(message);
    }
}
