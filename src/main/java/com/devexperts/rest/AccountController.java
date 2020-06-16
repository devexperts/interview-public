package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.exception.AccountBalanceException;
import com.devexperts.service.exception.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam(value = "source_id") long sourceId,
                                         @RequestParam(value = "target_id") long targetId,
                                         @RequestParam(value = "amount") double amount)
            throws AccountNotFoundException, AccountBalanceException {
        Account source = accountService.getAndCheckAccount(sourceId);
        Account target = accountService.getAndCheckAccount(targetId);
        accountService.transfer(source, target, amount);
        return new ResponseEntity("successful transfer", HttpStatus.OK);
    }
}
