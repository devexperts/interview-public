package com.devexperts.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = -1005331136714030524L;
    private UUID id;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
}
