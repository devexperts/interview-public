package com.devexperts.exeptions;

public class AccountAlreadyExistsExeption extends RuntimeException {
    public AccountAlreadyExistsExeption(){
        super("Account already exists");
    }

}
