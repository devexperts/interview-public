package com.devexperts.rest;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientAccountBalanceException;
import com.devexperts.service.AccountService;
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
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam long sourceId,
                                         @RequestParam long targetId,
                                         @RequestParam double amount) {
        try {
            accountService.transfer(accountService.getAccount(sourceId), accountService.getAccount(targetId), amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InsufficientAccountBalanceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
