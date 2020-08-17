package com.devexperts.error.model;

public class NotEnoughMoneyException extends Exception {
    private static final long serialVersionUID = 1801046243335480172L;

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
