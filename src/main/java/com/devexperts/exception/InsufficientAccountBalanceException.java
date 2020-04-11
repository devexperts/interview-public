package com.devexperts.exception;

public class InsufficientAccountBalanceException extends RuntimeException {
    public InsufficientAccountBalanceException(String message) {
        super(message);
    }
}
