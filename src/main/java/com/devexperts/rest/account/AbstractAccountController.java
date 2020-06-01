package com.devexperts.rest.account;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public abstract class AbstractAccountController {

    abstract ResponseEntity<Void> transfer(Long sourceId, Long targetId, BigDecimal amount);

}
