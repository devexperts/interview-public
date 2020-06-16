package com.devexperts.validator;

import com.devexperts.exception.InvalidAmountException;
import org.junit.Test;

public class AmountValidatorTest {

    @Test(expected = InvalidAmountException.class)
    public void validateAmount_shouldThrowException_negativeInpit() {
        AmountValidator.validateAmount(-1);
    }

    @Test
    public void validateAmount_shouldBeOk_zeroInpit() {
        AmountValidator.validateAmount(0);
    }

    @Test
    public void validateAmount_shouldBeOk_positiveInpit() {
        AmountValidator.validateAmount(5);
    }
}