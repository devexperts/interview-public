package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.AmountIsInvalidException;
import com.devexperts.exceptions.InsufficientAccountBalanceException;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam long sourceId,
                                         @RequestParam long targetId,
                                         @RequestParam double amount) {
        try {
            accountService.transferWithChecks(sourceId, targetId, amount);
        } catch (AmountIsInvalidException e) {
            return ResponseEntity.badRequest().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientAccountBalanceException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }
}
