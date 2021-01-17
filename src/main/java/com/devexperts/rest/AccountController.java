package com.devexperts.rest;

import com.devexperts.service.AccountService;
import com.devexperts.service.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations/transfer")
public class AccountController extends AbstractAccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(@RequestParam(name = "source_id", defaultValue = "0") long sourceId,
                                         @RequestParam(name = "target_id", defaultValue = "0") long targetId,
                                         @RequestParam(defaultValue = "0") double amount) {

        if (sourceId == 0 || targetId == 0 || amount == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (accountService.getAccount(sourceId) == null || accountService.getAccount(targetId) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        try {
            accountService.transfer(accountService.getAccount(sourceId), accountService.getAccount(targetId), amount);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (InsufficientBalanceException ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
