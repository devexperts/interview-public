package com.devexperts.rest.pojo;

import com.devexperts.rest.validation.ExistingAccountId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferOperationDTO {

    @ExistingAccountId
    private Long sourceId;

    @ExistingAccountId
    private Long targetId;

    @NotNull
    @Positive
    private Double amount;
}
