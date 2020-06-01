package com.devexperts.service.account;

public class TransferResult {
    private final TransferState state;

    public TransferResult(TransferState state) {
        this.state = state;
    }

    public static TransferResult state(TransferState state) {
        return new TransferResult(state);
    }

    public TransferState getState() {
        return state;
    }

    public enum TransferState {
        SUCCESS,
        WRONG_AMOUNT,
        SAME_ACCOUNT,
        ACCOUNT_NOT_FOUND,
        INSUFFICIENT_BALANCE,
        UNKNOWN
    }
}

