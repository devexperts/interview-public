package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import com.devexperts.service.exceptions.RecreateAccountException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class AccountServiceImpl implements AccountService {
    Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
        log.warn("account storage cleared");
    }

    @Override
    public void createAccount(Account account) throws RecreateAccountException {
        if (account == null) {
            if (log.isDebugEnabled()) { //performance +
                log.debug("null as parameter for createAccount method is not allowed");
            }
            throw new IllegalArgumentException("null as parameter for createAccount method is not allowed");
        }
        if (accounts.containsKey(account.getAccountKey())) {
            if (log.isDebugEnabled()) { //performance +
                log.debug("attempt to recreate an existing account with key={}", account.getAccountKey());
            }
            throw new RecreateAccountException("attempt to recreate an existing account");
        }
        accounts.put(account.getAccountKey(), account);
        if (log.isDebugEnabled()) { //performance +
            log.debug("account with key={} successfully created", account.getAccountKey());
        }
    }

    @Override
    public Account getAccount(long id) throws GetAccountException {
        Account account = accounts.get(AccountKey.valueOf(id));
        if (account == null) {
            if (log.isWarnEnabled()) { //performance +
                log.warn("account with key={} not find", AccountKey.valueOf(id));
            }
            throw new GetAccountException("account with key" + AccountKey.valueOf(id) + " not find");
        }
        return account;
    }

    @Override
    public void transfer(Account source, Account target, double amount) throws AccountsTransferAmountException {
        lockAccountBalance(source);
        lockAccountBalance(target);
        //after accounts locked we can do transfer
        try {
            doUnsafeTransferWithRollback(source, target, amount);
        } catch (Exception e) {
            unLockAccountBalance(source);
            lockAccountBalance(target);
            throw e;
        }
        unLockAccountBalance(source);
        lockAccountBalance(target);
    }

    private void doUnsafeTransferWithRollback(Account source, Account target, double amount) throws AccountsTransferAmountException {
        //save previous balance for simple rollback
        double sourceBalance = source.getBalance(); //BigDecimal for raise accuracy
        double targetBalance = target.getBalance(); //BigDecimal for raise accuracy
        try {
            if (source.getBalance() > amount) {
                source.setBalance(sourceBalance - amount);
                target.setBalance(targetBalance + amount);
                if (log.isInfoEnabled()) { //performance +
                    log.info("transfer successfully for accounts with key={} and key={}, amount={} ", source.getAccountKey(), target.getAccountKey(), amount);
                }
            } else {
                if (log.isWarnEnabled()) { //performance +
                    log.warn("transfer fail, not enough money in source account, accounts with key={} and key={}, amount={} ", source.getAccountKey(), target.getAccountKey(), amount);
                }
            }
        } catch (Exception e1) {
            if (log.isErrorEnabled()) { //performance +
                log.error("transfer fail, need rollback, accounts with key={} and key={}, amount={} exception={}", source.getAccountKey(), target.getAccountKey(), amount, e1);
            }
            rollback(source, target, amount, sourceBalance, targetBalance);
            if (log.isErrorEnabled()) { //performance +
                log.error("rollback complete good, accounts with key={} and key={}, amount={} exception={}", source.getAccountKey(), target.getAccountKey(), amount, e1);
            }
            throw new AccountsTransferAmountException("transfer fail, but rollback complete good");
        }
    }

    private void rollback(Account source, Account target, double amount, double sourceBalance, double targetBalance) throws AccountsTransferAmountException {
        try { //rollback
            source.setBalance(sourceBalance);
            target.setBalance(targetBalance);
        } catch (Exception e2) {
            if (log.isErrorEnabled()) { //performance +
                log.error("rollback fail, CALL TO DOCTOR, accounts with key={} and key={}, amount={} exception={}", source.getAccountKey(), target.getAccountKey(), amount, e2);
                throw new AccountsTransferAmountException("rollback fail, CALL TO DOCTOR");
            }
        }
    }

    private void lockAccountBalance(Account source) throws AccountsTransferAmountException {
        if (!source.lockBalance()) {
            if (log.isErrorEnabled()) { //performance +
                log.error("concurrent balance modification error with account {} ", source.getAccountKey());
                throw new AccountsTransferAmountException("concurrent balance modification error with account" + source.getAccountKey());
            }
        }
    }

    private void unLockAccountBalance(Account acc) throws AccountsTransferAmountException {
        if (!acc.unLockBalance()) {
            if (log.isErrorEnabled()) { //performance +
                log.error("unlock account {} fail ", acc.getAccountKey());
                throw new AccountsTransferAmountException("unlock account " + acc.getAccountKey() + " fail ");
            }
        }
    }
}
