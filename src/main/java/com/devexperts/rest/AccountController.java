package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController
{
    private final Log log = LogFactory.getLog(AccountController.class);
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/operations/transfer")
    public ResponseEntity<Void> transfer(
        @RequestParam("source_id") long sourceId
      , @RequestParam("target_id") long targetId
      , @RequestParam("amount") double amount)
    {
        log.debug(String.format("Going to transfer $%s from account %s to account %s", amount, sourceId, targetId));
        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);
        accountService.transfer(source, target, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
