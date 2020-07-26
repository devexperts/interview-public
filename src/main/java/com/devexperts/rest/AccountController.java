package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.rest.data.ResponseMessage;
import com.devexperts.service.AccountService;
import com.devexperts.service.exceptions.NegativeBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService( AccountService accountService ) {
        this.accountService = accountService;
    }

    //For test
    @Deprecated
    @GetMapping("test/init")
    public ResponseEntity<Void> testInit() {
        Account account1 = new Account( AccountKey.valueOf( 1 ), "Sergey", "Ivanov", 12d );
        Account account2 = new Account( AccountKey.valueOf( 2 ), "Vika", "Okulist", 125.0 );
        Account account3 = new Account( AccountKey.valueOf( 3 ), "Nikoly", "Frolov", 0.3d );
        accountService.createAccount( account1 );
        accountService.createAccount( account2 );
        accountService.createAccount( account3 );

        return ResponseEntity.ok().build();
    }

    @PostMapping("operations/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam("source_id") long sourceId,
            @RequestParam("target_id") long targetId,
            @RequestParam("amount") double amount
    ) {
        Account source = accountService.getAccount( sourceId );
        Account target = accountService.getAccount( targetId );

        if ( source == null || target == null ) {
            throw new NullPointerException ("account is not found");
        }

        accountService.transfer( source, target, amount );
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ResponseMessage> handleIllegalArgumentException( IllegalArgumentException ex ) {
        return ResponseEntity.badRequest().body(
                new ResponseMessage( ex.getMessage(),
                        400, "BAD_REQUEST", "/operations/transfer")
        );
    }

    @ExceptionHandler({NegativeBalanceException.class})
    public ResponseEntity<ResponseMessage> handleNegativeBalanceException( NegativeBalanceException ex ) {
        return ResponseEntity.status( 500 ).body(
                new ResponseMessage( ex.getMessage(),
                        500, "INTERNAL_SERVER_ERROR", "/operations/transfer")
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ResponseMessage> handleNullPointerException( NullPointerException ex ) {
        return ResponseEntity.status( 404 ).body(
                new ResponseMessage( ex.getMessage(),
                        404, "NOT_FOUND", "/operations/transfer")
        );
    }
}
