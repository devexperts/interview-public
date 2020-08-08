package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController (AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping(value = "/operations/transfer")
    public ResponseEntity<Void> transfer (@RequestBody long sourceId, @RequestBody long targetId,
                                         @RequestBody double amount) {

        if ((sourceId == 0) || (targetId == 0) || (amount <= 0.0)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account sourceAccount = accountService.getAccount(sourceId);
        Account targetAccount = accountService.getAccount(targetId);

        if ((sourceAccount == null) || (targetAccount == null)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (sourceAccount.getBalance() < amount){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        accountService.transfer(sourceAccount, targetAccount, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
