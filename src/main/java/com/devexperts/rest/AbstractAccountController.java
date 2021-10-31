package com.devexperts.rest;

import com.devexperts.rest.pojo.TransferOperationDTO;
import org.springframework.http.ResponseEntity;

public abstract class AbstractAccountController {
    abstract ResponseEntity<?> transfer(TransferOperationDTO transferOperation);
}
