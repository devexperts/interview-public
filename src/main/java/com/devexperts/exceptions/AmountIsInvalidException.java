package com.devexperts.exceptions;

public class AmountIsInvalidException  extends Exception {
    public AmountIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountIsInvalidException(String message) {
        super(message);
    }
}
