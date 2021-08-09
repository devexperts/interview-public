package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.exceptions.AccountNotFoundException;
import com.devexperts.service.exceptions.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
            @RequestParam("sourceId") Long sourceId,
            @RequestParam("targetId") Long targetId,
            @RequestParam("amount") BigDecimal amount) {

        if (sourceId == null || targetId == null || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // wrong parameters
        }
        try {
            Account source = accountService.getAccount(sourceId);
            Account target = accountService.getAccount(targetId);
            accountService.transfer(source,target,amount);
            return new ResponseEntity<>(HttpStatus.OK); // successful transfer
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // account is not found
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // insufficient account balance
        }
    }
}
