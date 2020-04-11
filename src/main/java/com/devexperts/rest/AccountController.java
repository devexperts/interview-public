package com.devexperts.rest;

import com.devexperts.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(@RequestBody @Valid Transfer transfer) {
        accountService.transfer(transfer.getSourceId(), transfer.getTargetId(), transfer.getAmount());

        return ResponseEntity.ok().build();
    }
}
