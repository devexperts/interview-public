package com.devexperts.exceptions;

public class InvalidAmountException extends TransferException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
