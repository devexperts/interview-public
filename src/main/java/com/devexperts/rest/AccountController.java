package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.exception.NotFoundAccountException;
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

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam Long sourceId, @RequestParam Long targetId, @RequestParam Double amount) {
        if (sourceId == null || targetId == null || amount == null || amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Account source = accountService.getAccount(sourceId);
            Account target = accountService.getAccount(targetId);
            accountService.transfer(source, target, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
