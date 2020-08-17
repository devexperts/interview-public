package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.error.model.AccountNotFoundException;
import com.devexperts.error.model.NotValidAmountException;
import com.devexperts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {
    private final AccountService accountService;
    private final ExecutorService transferExecutor;

    @Autowired
    public AccountController(AccountService accountService,
                             @Value("${application.transfer.executor.thread-pool-max-size}") int maxPoolSize,
                             @Value("${application.transfer.executor.thread-ttl-time-seconds}") long threadTTL) {
        this.accountService = accountService;
        this.transferExecutor = new ThreadPoolExecutor(0,
                maxPoolSize,
                threadTTL,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    @PostMapping(value = "/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam UUID sourceId,
                                         @RequestParam UUID targetId,
                                         @RequestParam BigDecimal amount) throws ExecutionException, InterruptedException {


        validateParameters(sourceId, targetId, amount);
        log.info("Receiving request {} {} {}", sourceId, targetId, amount);
        return transferExecutor
                .submit(() -> {
                    log.info("Executing task for {} {} {}", sourceId, targetId, amount);
                    Account sourceAccount = accountService.getAccount(sourceId);
                    Account targetAccount = accountService.getAccount(targetId);
                    if (sourceAccount == null) {
                        throw new AccountNotFoundException(sourceId);
                    }

                    if (targetAccount == null) {
                        throw new AccountNotFoundException(targetId);
                    }

                    accountService.transfer(sourceAccount, targetAccount, amount);

                    return ResponseEntity
                            .ok()
                            .<Void>build();
                })
                .get();
    }

    protected void validateParameters(UUID source, UUID target, BigDecimal amount) {
        if (source == null) {
            throw new IllegalArgumentException("Source account can't be null");
        }

        if (target == null) {
            throw new IllegalArgumentException("Target account can't be null");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Amount can't be null");
        }
    }
}
