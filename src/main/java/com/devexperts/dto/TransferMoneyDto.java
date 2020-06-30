package com.devexperts.dto;

import javax.validation.constraints.NotNull;

public class TransferMoneyDto {

    @NotNull
    private Long sourceId;
    @NotNull
    private Long targetId;
    private double amount;

    public TransferMoneyDto(Long sourceId, Long targetId, double amount) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
