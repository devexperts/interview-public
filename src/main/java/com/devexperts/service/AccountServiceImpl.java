package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.dao.AccountsDB;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import com.devexperts.service.exceptions.RecreateAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    public AccountsDB accountsDB;

    @Override
    public void clear() {
        accountsDB.deleteAll();
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
        if (accountsDB.existsById(account.getAccountKey())) {
            if (log.isDebugEnabled()) { //performance +
                log.debug("attempt to recreate an existing account with key={}", account.getAccountKey());
            }
            throw new RecreateAccountException("attempt to recreate an existing account");
        }

        accountsDB.save(account);
        if (log.isDebugEnabled()) { //performance +
            log.debug("account with key={} successfully created", account.getAccountKey());
        }
    }

    @Override
    public Account getAccount(long id) throws GetAccountException {
        Optional<Account> accountOptional = accountsDB.findById(AccountKey.valueOf(id));
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            if (log.isWarnEnabled()) { //performance +
                log.warn("account with key={} not find", AccountKey.valueOf(id));
            }
            throw new GetAccountException("account with key" + AccountKey.valueOf(id) + " not find");
        }
    }

    /**
     * Method throws exception when concurrent modification detected
     * no time locks(Synchronized, ReentrantLock ...) (как в описании вакансии - использование lock-free алгоритмов :-) )
     */
    @Override
    @Transactional
    public void transfer(Account source, Account target, double amount) throws AccountsTransferAmountException {
        //after accounts locked we can do transfer
        try {
            doUnsafeTransferWithRollback(source, target, amount);
        } catch (Exception e) {
            throw e;
        }
    }

    private void doUnsafeTransferWithRollback(Account source, Account target, double amount) throws AccountsTransferAmountException {
        AccountsTransferAmountException needToThrow = null;
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
                needToThrow = new AccountsTransferAmountException("transfer fail, not enough money in source account");
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
        if (needToThrow != null) throw needToThrow;
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
