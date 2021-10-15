package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountServiceImpl;
import com.devexperts.service.exceptions.IncorrectAmountOfTransfer;
import com.devexperts.service.exceptions.IncorrectTargetOfTransfer;
import com.devexperts.service.exceptions.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;

        //TODO remove after adding proper way to load accounts into accountService
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Petr", "Petrov", 200.0));
        accountService.createAccount(new Account(AccountKey.valueOf(3), "Pavel", "Pavlov", 300.0));
    }

    @RequestMapping(value = "/operations/transfer", method = RequestMethod.POST)
    public ResponseEntity<Void> transfer(@RequestParam long sourceId, @RequestParam long targetId, @RequestParam double amount) {
        try {
            accountService.transferThreadSafely(
                    accountService.getAccount(sourceId),
                    accountService.getAccount(targetId),
                    amount);

        } catch (IncorrectAmountOfTransfer ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException | IncorrectTargetOfTransfer ex) {
            // also executes if source and target are equal
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InsufficientFundsException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
