package com.devexperts.error.handler;

import com.devexperts.error.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.RejectedExecutionException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class BankAccountsExceptionHandler {

    @ExceptionHandler({NotEnoughMoneyException.class})
    ResponseEntity<ErrorEntity> handleNotEnoughMoneyException(NotEnoughMoneyException exception) {
        ErrorEntity errorEntity = ErrorEntity
                .builder()
                .code(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()).toString())
                .message(String.format("%s", exception.getMessage()))
                .build();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(errorEntity);
    }

    @ExceptionHandler({RejectedExecutionException.class})
    ResponseEntity<ErrorEntity> handleRejectionExecutionException(RejectedExecutionException exception) {
        ErrorEntity errorEntity = ErrorEntity.builder().build();
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        return ResponseEntity
                .status(status)
                .body(errorEntity);
    }

    @ExceptionHandler({AccountNotFoundException.class})
    ResponseEntity<ErrorEntity> handleAccountNotFoundException(AccountNotFoundException exception) {
        ErrorEntity errorEntity = ErrorEntity
                .builder()
                .code(Integer.valueOf(HttpStatus.NOT_FOUND.value()).toString())
                .message(String.format("Account not found %s", exception.getId()))
                .build();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(errorEntity);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    ResponseEntity<ErrorEntity> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorEntity errorEntity = ErrorEntity
                .builder()
                .code(Integer.valueOf(HttpStatus.BAD_REQUEST.value()).toString())
                .message(exception.getMessage())
                .build();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(errorEntity);
    }

    @ExceptionHandler({NotValidAmountException.class})
    ResponseEntity<ErrorEntity> handleNotValidAmountException(NotValidAmountException exception) {
        ErrorEntity errorEntity = ErrorEntity
                .builder()
                .code(Integer.valueOf(HttpStatus.BAD_REQUEST.value()).toString())
                .message(String.format("%s %s", exception.getMessage(), exception.getAmount()))
                .build();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(errorEntity);
    }
}
