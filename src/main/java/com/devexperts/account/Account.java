package com.devexperts.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Accounts")
public class Account {
    @Setter
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    private long accountId;

    private final String firstName;

    private final String lastName;

    @Setter
    private Double balance = 0.0;
}
