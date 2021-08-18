package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {
    private final AccountServiceImpl service;
    @Autowired
    public AccountController(AccountServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/operations/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam(value = "sourceId") Long sourceId,
            @RequestParam(value = "targetId") Long targetId,
            @RequestParam(value = "amount") Double amount) {
        if(sourceId == null || targetId == null || amount == null)
            return new ResponseEntity<>(
                    "One of the parameters in not present or amount is invalid",
                    HttpStatus.BAD_REQUEST);

        Account source = service.getAccount(sourceId);
        Account target = service.getAccount(targetId);
        if (source == null || target == null)
            return new ResponseEntity<>(
                    "Account is not found", HttpStatus.NOT_FOUND);

        try {
            service.transfer(source, target, amount);
            return new ResponseEntity<>(
                    "Successful transfer", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    "Insufficient account balance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
