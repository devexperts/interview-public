package com.devexperts.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TransferRecordDTO implements Serializable {
    private static final long serialVersionUID = 4071677001448162552L;
    private UUID id;
    private UUID sourceId;
    private UUID targetId;
    private BigDecimal amount;
    private OffsetDateTime transferTime;
}
