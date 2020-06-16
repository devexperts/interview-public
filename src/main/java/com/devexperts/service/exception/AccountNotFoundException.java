package com.devexperts.service.exception;

public class AccountNotFoundException extends AccountServiceException{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
