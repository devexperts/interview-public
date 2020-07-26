package com.devexperts.service.exceptions;

public class NegativeBalanceException extends IllegalArgumentException {

    public NegativeBalanceException() {
        super();
    }

    public NegativeBalanceException(String s) {
        super(s);
    }

    public NegativeBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeBalanceException(Throwable cause) {
        super(cause);
    }
}
