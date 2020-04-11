package com.devexperts.rest;

import javax.validation.constraints.Min;

public class Transfer {
    private final long sourceId;
    private final long targetId;
    @Min(value = 1)
    private final double amount;

    public Transfer(long sourceId, long targetId, double amount) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public double getAmount() {
        return amount;
    }
}
