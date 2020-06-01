package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.account.TransferResult.TransferState;
import com.devexperts.service.lock.LockService.LockNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountCacheService cacheService;
    private final AccountLockService lockService;

    public AccountServiceImpl(AccountCacheService cacheService,
                              AccountLockService lockService) {
        this.cacheService = cacheService;
        this.lockService = lockService;
    }

    @Override
    public void clear() {
        cacheService.clear();
        log.info("Cache cleared");
    }

    @Override
    public void createAccount(@NotNull Account account) {
        Account oldAccount = cacheService.put(account.getAccountKey(), account);
        if (oldAccount != null) {
            throw new IllegalArgumentException(String.format("Account for key [%s] is already present", account.getAccountKey()));
        }

        log.info("Create new account [{}]", account);
    }

    @Override
    public Account getAccount(@NotNull AccountKey key) {
        return cacheService.get(key);
    }

    @NotNull
    @Override
    public TransferResult transfer(@NotNull AccountKey sourceKey, @NotNull AccountKey targetKey, @NotNull BigDecimal amount) {
        if (sourceKey.equals(targetKey)) {
            return TransferResult.state(TransferState.SAME_ACCOUNT);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return TransferResult.state(TransferState.WRONG_AMOUNT);
        }

        try {
            return lockService.lock(sourceKey, targetKey, (accFrom, accTo) -> {
                if (!accFrom.hasAmount(amount)) {
                    log.info("Account [{}] has insufficient balance for transfer from: [{}] to: [{}] amount: [{}]", accFrom, sourceKey, targetKey, amount);
                    return TransferResult.state(TransferState.INSUFFICIENT_BALANCE);
                }

                accFrom.withdrawAmount(amount);
                accTo.depositAmount(amount);

                log.info("Transfer [{}] from [{}] to [{}]", amount, accFrom, accTo);
                return TransferResult.state(TransferState.SUCCESS);
            });
        } catch (LockNotFoundException e) {
            log.info("Account key [{}] not found during transfer from: [{}] to: [{}] amount: [{}]", e.getLock(), sourceKey, targetKey, amount);
            return TransferResult.state(TransferState.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            log.info("Unknown exception during transfer from: [{}] to: [{}] amount: [{}]", sourceKey, targetKey, amount, e);
        }

        return TransferResult.state(TransferState.UNKNOWN);
    }


}
