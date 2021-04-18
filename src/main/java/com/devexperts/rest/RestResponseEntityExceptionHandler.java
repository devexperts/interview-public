package com.devexperts.rest;

import com.devexperts.rest.exceptions.AccountNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@SuppressWarnings("unused")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String RESPONSE_BODY_TEMPLATE = "{ \"status\": \"failed\", \"description\": \"%s\" }";

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException e, WebRequest request) {
        String responseBody = String.format(RESPONSE_BODY_TEMPLATE, e.getMessage());
        return responseEntity(e, request, HttpStatus.BAD_REQUEST, responseBody);
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFoundException(RuntimeException e, WebRequest request) {
        String responseBody = String.format(RESPONSE_BODY_TEMPLATE, e.getMessage());
        return responseEntity(e, request, HttpStatus.NOT_FOUND, responseBody);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleInternalErrors(RuntimeException e, WebRequest request) {
        String responseBody = String.format(RESPONSE_BODY_TEMPLATE, e.getMessage());
        return responseEntity(e, request, HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
    }

    private ResponseEntity<Object> responseEntity(RuntimeException e, WebRequest request, HttpStatus httpStatus, String responseBody) {
        return handleExceptionInternal(e, responseBody, new HttpHeaders(), httpStatus, request);
    }
}
