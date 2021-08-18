package com.devexperts.rest;

import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<String> transfer(Long sourceId, Long targetId, Double amount);
}
