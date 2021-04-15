package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.exeptions.NotEnoughFundsException;
import com.devexperts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping(path = "/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam("source_id") long sourceId,
                                         @RequestParam("target_id") long targetId,
                                         @RequestParam("amount") double amount)
    {
        Account accountSource = accountService.getAccount(sourceId);
        Account accountTarget = accountService.getAccount(targetId);
        if (accountSource == null || accountTarget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            accountService.transfer(accountSource, accountTarget, BigDecimal.valueOf(amount));
        } catch (NotEnoughFundsException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
