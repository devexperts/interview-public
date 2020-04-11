package com.devexperts.rest;

import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(Transfer transfer);
}
