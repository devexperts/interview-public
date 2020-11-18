package com.devexperts.rest;

import com.devexperts.account.exceptions.InsufficientFundsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@ControllerAdvice(assignableTypes = AccountController.class)
public class AccountControllerAdvice extends ResponseEntityExceptionHandler
{
  private final Log log = LogFactory.getLog(AccountControllerAdvice.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(IllegalArgumentException.class)
  String handleIllegalArgumentException(IllegalArgumentException ex) {
    log.debug("IllegalArgumentException handled");
    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(InsufficientFundsException.class)
  String handleInsufficientFundsException(InsufficientFundsException ex) {
    log.debug("InsufficientFundsException handled");
    return ex.getMessage();
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.debug("MissingServletRequestParameterException handled");
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
