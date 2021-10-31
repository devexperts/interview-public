package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.rest.pojo.TransferOperationDTO;
import com.devexperts.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CommonsLog
@RestController
@AllArgsConstructor
@RequestMapping("/api/operations")
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    @PostMapping(value = "/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Valid @NotNull TransferOperationDTO transferOperation) {

        Account source = accountService.getAccount(transferOperation.getSourceId()),
                target = accountService.getAccount(transferOperation.getTargetId());

        try {
            accountService.transfer(source, target, transferOperation.getAmount());
        } catch (RuntimeException exception) {
            log.error("Couldn't complete transfer", exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }

        log.info("Transfer complete.");
        return ResponseEntity.ok("nice!");
    }
}
