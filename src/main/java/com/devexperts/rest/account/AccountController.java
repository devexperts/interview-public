package com.devexperts.rest.account;

import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.account.AccountService;
import com.devexperts.service.account.TransferResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Должны присутствовать какая-то аутентификация
    // Должна быть проверка на количество знаком после запятой в amount или принудительное округление
    // Для трансфера очень желательно иметь некий requestId, чтобы избегать дублирующих запросов и реализовывать принцип идемпотентности
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestParam Long sourceId, @RequestParam Long targetId, @RequestParam BigDecimal amount) {
        log.info("Receive transfer from: [{}] to: [{}] amount: [{}]", sourceId, targetId, amount);

        if (sourceId == null || sourceId <= 0 || targetId == null || targetId <= 0 || amount == null) {
            return ResponseEntity.badRequest().build();
        }

        AccountKey sourceKey = AccountKey.valueOf(sourceId);
        AccountKey targetKey = AccountKey.valueOf(targetId);
        TransferResult result = accountService.transfer(sourceKey, targetKey, amount);
        log.info("Get result [{}] for transfer from: [{}] to: [{}] amount: [{}]", result.getState(), sourceKey, targetKey, amount);

        switch (result.getState()) {
            case SUCCESS:
                return ResponseEntity.ok().build();
            case WRONG_AMOUNT:
            case SAME_ACCOUNT:
                return ResponseEntity.badRequest().build();
            case ACCOUNT_NOT_FOUND:
                return ResponseEntity.notFound().build();
            case INSUFFICIENT_BALANCE:
            case UNKNOWN:
            default:
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
