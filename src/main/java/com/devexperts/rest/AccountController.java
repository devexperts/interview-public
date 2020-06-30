package com.devexperts.rest;

import com.devexperts.dto.TransferMoneyDto;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("operations/transfer")
    public ResponseEntity<Void> transfer(@Valid TransferMoneyDto transferMoneyDto) {
        service.transfer(transferMoneyDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
