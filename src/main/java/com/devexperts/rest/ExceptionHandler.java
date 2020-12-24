package com.devexperts.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.devexperts.exception.AccountNotRegisteredException;
import com.devexperts.exception.NotEnoughAmountException;
import com.devexperts.exception.ParametersInvalidException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{
	
	@org.springframework.web.bind.annotation.ExceptionHandler(AccountNotRegisteredException.class)
    protected ResponseEntity<Object> accountNotRegistered(AccountNotRegisteredException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.NOT_FOUND,
                request
        );
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = NotEnoughAmountException.class)
    protected ResponseEntity<Object> amountNotEnough(NotEnoughAmountException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

	@org.springframework.web.bind.annotation.ExceptionHandler(value = ParametersInvalidException.class)
    protected ResponseEntity<Object> invalidParams(ParametersInvalidException e, WebRequest request) {
        return handleExceptionInternal(
                e,
                e.getMessage(),
                HttpHeaders.EMPTY,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

}
