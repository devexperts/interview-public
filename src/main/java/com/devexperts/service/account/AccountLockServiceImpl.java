package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountLockServiceImpl implements AccountLockService {
    private static final Logger log = LoggerFactory.getLogger(AccountLockServiceImpl.class);

    private final AccountCacheService cacheService;

    @Autowired
    public AccountLockServiceImpl(AccountCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public <R> R lock(AccountKey key, LockCallable<R, Account> callable) {
        return cacheService.cacheLockExecute(() -> {
            final Account account = cacheService.get(key);
            if (account == null) {
                log.debug("Lock not found for " + key);
                throw new LockNotFoundException(key);
            }
            synchronized (account) {
                return callable.call(account);
            }
        });
    }

    // Лобовое решение. Где-то более уместно использовать reentrantLock + tryLock в цикле. Или какую-то реализацию для распределенных транзакций
    @Override
    public <R> R lock(AccountKey key1, AccountKey key2, LocksCallable<R, Account> callable) {
        return cacheService.cacheLockExecute(() -> {
            final Account account1 = cacheService.get(key1);
            if (account1 == null) {
                log.debug("Lock not found for " + key1);
                throw new LockNotFoundException(key1);
            }

            if (key1.compareTo(key2) == 0) {
                synchronized (account1) {
                    return callable.call(account1, account1);
                }
            }

            final Account account2 = cacheService.get(key2);
            if (account2 == null) {
                log.debug("Lock not found for " + key2);
                throw new LockNotFoundException(key2);
            }

            if (key1.compareTo(key2) < 0) {
                synchronized (account1) {
                    synchronized (account2) {
                        return callable.call(account1, account2);
                    }
                }
            } else {
                synchronized (account2) {
                    synchronized (account1) {
                        return callable.call(account1, account2);
                    }
                }
            }
        });
    }

}
