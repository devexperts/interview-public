package com.devexperts.rest;

import com.devexperts.dto.TransferMoneyDto;
import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(TransferMoneyDto transferMoneyDto);
}
