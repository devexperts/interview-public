package com.devexperts.exceptions;

public class InsufficientBalanceException extends TransferException {
    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
