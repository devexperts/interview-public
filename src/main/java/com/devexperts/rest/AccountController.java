package com.devexperts.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@JsonArg("source_id") long sourceId,
                                         @JsonArg("target_id") long targetId,
                                         @JsonArg("amount") double amount) {
        return null;
    }
}
