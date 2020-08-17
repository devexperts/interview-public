package com.devexperts.rest;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(UUID sourceId, UUID targetId, BigDecimal amount) throws ExecutionException, InterruptedException;
}
