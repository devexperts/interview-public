package com.devexperts.service.exceptions;

public class IncorrectTargetOfTransfer extends Exception {
    private TypeOfIncorrection typeOfIncorrection;

    public IncorrectTargetOfTransfer(TypeOfIncorrection typeOfIncorrection) {
        this.typeOfIncorrection = typeOfIncorrection;
    }

    public String getTypeOfIncorrection() {
        return typeOfIncorrection.toString();
    }
}

