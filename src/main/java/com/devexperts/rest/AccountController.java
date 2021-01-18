package com.devexperts.rest;

import com.devexperts.exception.DataNotFoundException;
import com.devexperts.exception.InputDataValidationException;
import com.devexperts.facade.AccountFacade;
import com.devexperts.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/api/operations/transfer")
public class AccountController extends AbstractAccountController {

    @Autowired
    private AccountFacade accountFacade;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(@RequestParam(name = "source_id", defaultValue = "0") long sourceId,
                                         @RequestParam(name = "target_id", defaultValue = "0") long targetId,
                                         @RequestParam(defaultValue = "0") double amount) {

        accountFacade.transfer(sourceId, targetId, amount);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Void> onDataNotFoundException() {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InputDataValidationException.class)
    public ResponseEntity<Void> onInputDataValidationException() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientBalanceException.class)
    public ResponseEntity<Void> onInsufficientBalanceException() {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
