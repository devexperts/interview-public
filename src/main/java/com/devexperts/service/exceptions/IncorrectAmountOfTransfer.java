package com.devexperts.service.exceptions;

public class IncorrectAmountOfTransfer extends Exception {
    private double incorrectAmount;

    public IncorrectAmountOfTransfer(double incorrectAmount) {
        this.incorrectAmount = incorrectAmount;
    }

    public double getIncorrectAmount() {
        return incorrectAmount;
    }
}
