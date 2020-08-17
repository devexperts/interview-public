package com.devexperts.error.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class NotValidAmountException extends RuntimeException {
    private static final long serialVersionUID = -4129752690037127408L;
    private final BigDecimal amount;

    public NotValidAmountException(String message, BigDecimal amount) {
        super(message);
        this.amount = amount;
    }
}
