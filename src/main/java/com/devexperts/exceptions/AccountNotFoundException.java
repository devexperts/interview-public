package com.devexperts.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
