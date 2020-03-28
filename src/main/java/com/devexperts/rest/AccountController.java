package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

  private AccountServiceImpl accountService;

  @Autowired
  AccountController(AccountServiceImpl accountService) {
    this.accountService = accountService;
  }

  /**
   * Details: The operation should be available at POST localhost:8080/api/operations/transfer with
   * required query parameters: source_id, target_id, amount. Response codes are the following:
   * <p>
   * 200 (OK) - successful transfer 400 (Bad Request) - one of the parameters in not present or
   * amount is invalid 404 (Not Found) - account is not found 500 (Internal Server Error) -
   * insufficient account balance
   *
   * @param sourceId
   * @param targetId
   * @param amount
   * @return
   */
  @PostMapping(value = "/operations/transfer")
  @Override
  public ResponseEntity<Void> transfer(@RequestParam long sourceId, @RequestParam long targetId,
      @RequestParam double amount) {

    if (amount < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Account sourceAccount = accountService.getAccount(sourceId);
    Account targetAccount = accountService.getAccount(targetId);

    if (sourceAccount == null || targetAccount == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (sourceAccount.getBalance() < amount) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      accountService.transfer(sourceAccount, targetAccount, amount);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
