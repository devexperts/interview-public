package com.devexperts.exceptions;

public class InsufficientBalanceException extends IllegalArgumentException{
    public InsufficientBalanceException(String s) {
        super(s);
    }
}
