package com.devexperts.rest;

import com.devexperts.rest.dto.ErrorResponse;
import com.devexperts.service.exceptions.AccountNotExistException;
import com.devexperts.service.exceptions.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(AccountNotExistException.class)
    public ResponseEntity<ErrorResponse> accountNotExist() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Account doesn't exist"));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> insufficientFunds() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Not enough money"));
    }


}
