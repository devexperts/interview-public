package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.rest.exceptions.AccountNotFoundException;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    public AccountController(@Autowired AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@JsonArg("source_id") long sourceId,
                                         @JsonArg("target_id") long targetId,
                                         @JsonArg("amount") double amount) {

        Account sourceAccount = accountService.getAccount(sourceId);
        if (sourceAccount == null) {
            throw new AccountNotFoundException("The source account was not found.");
        }

        Account targetAccount = accountService.getAccount(targetId);
        if (targetAccount == null) {
            throw new AccountNotFoundException("The target account was not found.");
        }

        accountService.transfer(sourceAccount, targetAccount, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
