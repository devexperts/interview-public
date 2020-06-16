package com.devexperts.validator;

import com.devexperts.exception.InvalidAmountException;

public final class AmountValidator {

    private AmountValidator() {}

    public static void validateAmount(double amount) {
        if(amount <= 0) {
            throw new InvalidAmountException("Only transfers with positive amount are allowed");
        }
    }
}
