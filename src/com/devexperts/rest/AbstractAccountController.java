package com.devexperts.rest;

import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(long sourceId, long targetId, double amount);
}
