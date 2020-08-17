package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.error.model.NotValidAmountException;
import com.devexperts.error.model.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    protected final Map<UUID, Account> accountsCache = new ConcurrentHashMap<>();
    protected final Object internalLock = new Object();

    @Autowired
    public AccountServiceImpl() {
    }

    @Override
    public void clear() {
        accountsCache.clear();
    }

    @Override
    public void createAccount(Account account) {
        accountsCache.putIfAbsent(account.getAccountKey().getAccountId(), account);
    }

    @Override
    public Account getAccount(UUID accountId) {
        return accountsCache.get(accountId);
    }

    @Override
    public void transfer(final Account source,
                         final Account target,
                         final BigDecimal amount) throws NotEnoughMoneyException {
        validateParameters(source, target, amount);
        int sourceHashCode = System.identityHashCode(source);
        int targetHashCode = System.identityHashCode(target);
        //Must to avoid deadlock when thread-1 calls transfer (source, target, amount_1) and other thread calls transfer (target, source, amount_2)
        if (sourceHashCode > targetHashCode) {
            synchronized (source) {
                synchronized (target) {
                    executeTransfer(source, target, amount);
                }
            }
        } else if (sourceHashCode < targetHashCode) {
            synchronized (target) {
                synchronized (source) {
                    executeTransfer(source, target, amount);
                }
            }
        } else {
            synchronized (internalLock) {
                synchronized (source) {
                    synchronized (target) {
                        executeTransfer(source, target, amount);
                    }
                }
            }
        }
    }

    protected void executeTransfer(Account source, Account target, BigDecimal amount) throws NotEnoughMoneyException {
        BigDecimal sourceBalance = source.getBalance();
        if (sourceBalance.compareTo(amount) < 0) {
            throw new NotEnoughMoneyException("Not enough balance to proceed with operation");
        }

        //simulate transactions
        accountsCache
                .get(source.getAccountKey().getAccountId())
                .setBalance(sourceBalance.subtract(amount));
        accountsCache
                .get(target.getAccountKey().getAccountId())
                .setBalance(target.getBalance().add(amount));
    }

    protected void validateParameters(Account source, Account target, BigDecimal amount) {
        if (source == null
                || source.getBalance() == null
                || source.getAccountKey() == null
                || source.getAccountKey().getAccountId() == null) {
            throw new IllegalArgumentException("Source account/balance can't be null");
        }

        if (target == null
                || target.getBalance() == null
                || target.getAccountKey() == null
                || target.getAccountKey().getAccountId() == null) {
            throw new IllegalArgumentException("Target account/balance can't be null");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Amount can't be null");
        }

        if (source.getAccountKey().getAccountId().equals(target.getAccountKey().getAccountId())) {
            throw new IllegalArgumentException("Source and target accounts shouldn't be equal to each other");
        }

        //Deliver such cases to a client code
        if (amount.signum() != 1) {
            throw new NotValidAmountException("Amount must be positive", amount);
        }
    }
}
