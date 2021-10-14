package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService service;

    @Autowired
    public AccountController (AccountService service) {
        this.service = service;
    }

    @GetMapping("operations/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam("sourceId") Long sourceId,
            @RequestParam("targetId") Long targetId,
            @RequestParam("amount") Double amount) {
        if (sourceId == null || targetId == null || amount < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            Account source = service.getAccount(sourceId);
            Account target = service.getAccount(targetId);
            service.transfer(source, target, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}