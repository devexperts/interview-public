package com.devexperts.service.exceptions;

public class InsufficientFundsException extends Exception {

    private double lackOfFunds;

    public InsufficientFundsException(double lack) {
        this.lackOfFunds = lack;
    }

    public double getLackOfFunds() {
        return lackOfFunds;
    }
}
