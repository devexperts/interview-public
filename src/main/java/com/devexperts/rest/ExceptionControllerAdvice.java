package com.devexperts.rest;

import com.devexperts.service.exception.AccountBalanceException;
import com.devexperts.service.exception.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountBalanceException.class)
    public ResponseEntity accountBalanceExceptionHandling(Throwable t) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity accountNotFoundExceptionHandling(Throwable t) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(t.getMessage());
    }

}
