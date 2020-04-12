package com.devexperts.exception;

/**
 * Indicates that specific account has insufficient balance for an operation.
 */
public class InsufficientAccountBalanceException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public InsufficientAccountBalanceException(String s) {
        super(s);
    }
}
