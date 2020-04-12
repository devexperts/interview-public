package com.devexperts.rest;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientAccountBalanceException;
import com.devexperts.exception.InvalidBalanceException;
import com.devexperts.exception.InvalidTransferAccountException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AccountExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = AccountNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(
            value = {InvalidTransferAccountException.class, InvalidBalanceException.class})
    protected ResponseEntity<Object> handleInvalidTransferAccount(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InsufficientAccountBalanceException.class)
    protected ResponseEntity<Object> handleInsufficientAccountBalance(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

}