package com.devexperts.handler;

import com.devexperts.dto.ErrorDto;
import com.devexperts.exception.BadRequestException;
import com.devexperts.exception.InsufficentFundsException;
import com.devexperts.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequests(BadRequestException exception) {
        return convertExceptionToErrorDto(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleBadRequests(NotFoundException exception) {
        return convertExceptionToErrorDto(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(InsufficentFundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleBadRequests(InsufficentFundsException exception) {
        return convertExceptionToErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final Map<String, String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return convertExceptionToErrorDto(HttpStatus.BAD_REQUEST, errorMessages.toString());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(NumberFormatException exception) {
        return convertExceptionToErrorDto(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private static ErrorDto convertExceptionToErrorDto(final HttpStatus status, final String message) {
        return new ErrorDto(message, status, LocalDateTime.now());
    }
}
