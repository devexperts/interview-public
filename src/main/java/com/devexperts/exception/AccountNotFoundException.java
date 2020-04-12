package com.devexperts.exception;

/**
 * Indicates that specific account was not found.
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public AccountNotFoundException(String s) {
        super(s);
    }
}
