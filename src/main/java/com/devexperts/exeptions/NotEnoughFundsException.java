package com.devexperts.exeptions;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(){
        super("Not Enough Funds");
    }
}
