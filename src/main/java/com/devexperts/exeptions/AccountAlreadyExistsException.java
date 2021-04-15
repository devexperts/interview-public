package com.devexperts.exeptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(){
        super("Account already exists");
    }

}
