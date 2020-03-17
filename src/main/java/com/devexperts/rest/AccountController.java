package com.devexperts.rest;

import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    @Autowired
    private AccountService accountService;

    @Override
    @RequestMapping(value = "/operations/transfer", method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(long sourceId, long targetId, double amount) {

        if (sourceId == 0 || targetId == 0 || amount <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            accountService.transfer(sourceId, targetId, amount);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (InternalError ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
