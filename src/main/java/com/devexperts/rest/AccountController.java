package com.devexperts.rest;

import com.devexperts.service.AccountService;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountController extends AbstractAccountController {
    AccountService accountService;

    public AccountController(@Autowired AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/api/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam long sourceId, @RequestParam long targetId, @RequestParam double amount) {
        //response 400 is automatic
        try {
            accountService.transfer(accountService.getAccount(sourceId), accountService.getAccount(targetId), amount);
        } catch (AccountsTransferAmountException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //response 500
        } catch (GetAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //response 404
        }
        return ResponseEntity.ok().build(); //response 200
    }
}
