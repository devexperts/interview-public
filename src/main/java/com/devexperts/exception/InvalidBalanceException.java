package com.devexperts.exception;

/**
 * Indicates balance would be invalid after specific operation.
 */
public class InvalidBalanceException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public InvalidBalanceException(String s) {
        super(s);
    }
}
