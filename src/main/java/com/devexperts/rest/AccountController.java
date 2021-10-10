package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.exceptions.InsufficientBalanceException;
import com.devexperts.exceptions.InvalidAmountException;
import com.devexperts.exceptions.TransferException;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {
    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam("source_id") long sourceId,
                                         @RequestParam("target_id") long targetId,
                                         @RequestParam double amount) {
        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);
        if(source == null || target == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            accountService.transfer(source, target, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(InvalidAmountException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (TransferException e) {
            // all other errors are internal serer
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {

    }
}
