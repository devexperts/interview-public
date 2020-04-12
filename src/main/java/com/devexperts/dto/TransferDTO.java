package com.devexperts.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.DecimalMin;

@Getter
@RequiredArgsConstructor
public class TransferDTO {
    private final long sourceId;
    private final long targetId;
    @DecimalMin(value = "0", inclusive = false)
    private final double amount;
}
