package com.devexperts.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Account {
    private final long accountId;
    private final String firstName;
    private final String lastName;
    @Setter
    private Double balance;
}
