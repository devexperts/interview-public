package com.devexperts.exception;

/**
 * Indicates exceptions connected with transfer of funds between accounts.
 */
public class TransferException extends RuntimeException {

    /**
     * Constructs a {@code TransferException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param s the detail message.
     */
    public TransferException(String s) {
        super(s);
    }
}
