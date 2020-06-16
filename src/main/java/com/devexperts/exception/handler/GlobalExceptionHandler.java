package com.devexperts.exception.handler;

import com.devexperts.rest.controller.OperationsController;
import com.devexperts.exception.BalanceException;
import com.devexperts.exception.EntityNotFoundException;
import com.devexperts.exception.InvalidAmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(OperationsController.class);

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity handleBalanceException(BalanceException ex) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    protected ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleException(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ResponseEntity handleInvalidAmountException(InvalidAmountException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex);
    }

    private class ResponseEntity {
        private int code;
        private String message;

        public ResponseEntity(HttpStatus code, String message){
            this.code = code.value();
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private <T extends Exception> ResponseEntity handleException(HttpStatus statusCode, T exception) {
        logException(exception);
        return new ResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private <T extends Exception> void logException(T exception) {
        LOGGER.error(exception.getMessage(), exception);
    }
}
