package com.devexperts.rest;

import com.devexperts.account.Account;
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
        // one of the parameters in not present or amount is invalid
        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // account is not found
        Account ac1 = accountService.getAccount(sourceId);
        Account ac2 = accountService.getAccount(targetId);
        if (ac1 == null || ac2 == null) {
            return ResponseEntity.notFound().build();
        }

        // insufficient account balance
        if (ac1.getBalance() < amount) {
            return ResponseEntity.status(500).build();
        }

        accountService.transfer(ac1, ac2, amount);

        //successful transfer
        return ResponseEntity.ok().build();
    }
}
