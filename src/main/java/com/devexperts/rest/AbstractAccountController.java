package com.devexperts.rest;

import com.devexperts.service.exception.AccountBalanceException;
import com.devexperts.service.exception.AccountNotFoundException;

import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(long sourceId, long targetId, double amount)
            throws AccountNotFoundException, AccountBalanceException;
}
