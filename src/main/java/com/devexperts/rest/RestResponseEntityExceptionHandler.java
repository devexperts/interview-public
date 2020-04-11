package com.devexperts.rest;

import com.devexperts.exception.AccountNotFountException;
import com.devexperts.exception.InsufficientAccountBalanceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AccountNotFountException.class)
    protected ResponseEntity<Object> handleNotFound(AccountNotFountException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                "Account not found",
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = InsufficientAccountBalanceException.class)
    protected ResponseEntity<Object> handleInsufficientAccountBalance(InsufficientAccountBalanceException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                "Insufficient account balance",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }
}
