package com.devexperts.error.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AccountNotFoundException extends Exception {
    private static final long serialVersionUID = -6936713331922586386L;
    private final UUID id;

    public AccountNotFoundException(UUID id) {
        this.id = id;
    }
}
