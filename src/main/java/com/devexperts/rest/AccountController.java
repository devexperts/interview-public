package com.devexperts.rest;

import com.devexperts.exceptions.AccountDoesNotExistExceptions;
import com.devexperts.exceptions.InsufficientBalanceException;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam @NotNull long sourceId,
            @RequestParam @NotNull long targetId,
            @RequestParam @NotNull double amount) {
        try{
            accountService.transfer(accountService.getAccount(sourceId),accountService.getAccount(targetId),amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountDoesNotExistExceptions e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InsufficientBalanceException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
