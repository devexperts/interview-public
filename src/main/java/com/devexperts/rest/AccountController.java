package com.devexperts.rest;

import com.devexperts.dto.TransferDTO;
import com.devexperts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService accountService;

    // Note: It's pointless to have post method with query params instead of body for the information that needs to be
    // submitted. So I'm moving the parameters to request body. If there is necessity for query params that indicate
    // some options instead of submitted date - they could be added as query params.
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferDTO transfer) {
        accountService.transfer(accountService.getAccount(transfer.getSourceId()),
                accountService.getAccount(transfer.getTargetId()),
                transfer.getAmount());

        return ResponseEntity.ok().build();
    }
}
