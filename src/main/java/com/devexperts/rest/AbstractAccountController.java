package com.devexperts.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(@PathVariable Long sourceId, @PathVariable Long targetId, @PathVariable Double amount);
}
