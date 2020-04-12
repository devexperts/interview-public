package com.devexperts.rest;

import com.devexperts.dto.TransferDTO;
import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<Void> transfer(TransferDTO transfer);
}
