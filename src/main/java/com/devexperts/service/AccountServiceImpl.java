package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.dao.AccountsDB;
import com.devexperts.service.dao.TransfersDB;
import com.devexperts.service.exceptions.AccountsTransferAmountException;
import com.devexperts.service.exceptions.GetAccountException;
import com.devexperts.service.exceptions.RecreateAccountException;
import com.devexperts.account.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountsDB accountsDB;
    @Autowired
    private TransfersDB transfersDB;

    @Override
    public void clear() {
        accountsDB.deleteAll();
        log.warn("account storage cleared");
    }

    public AccountServiceImpl() {
    }

    public AccountServiceImpl(AccountsDB accountsDB, TransfersDB transfersDB) {
        this.accountsDB = accountsDB;
        this.transfersDB = transfersDB;
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
        doTransfer(source, target, amount);
        transfersDB.save(new Transfer(source.getAccountKey().getAccountId(), target.getAccountKey().getAccountId(), amount));
    }

    private void doTransfer(Account source, Account target, double amount) throws AccountsTransferAmountException {
        AccountsTransferAmountException needToThrow = null;
        try {
            double sourceBalance = source.getBalance(); //BigDecimal for raise accuracy
            double targetBalance = target.getBalance(); //BigDecimal for raise accuracy
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
            throw new AccountsTransferAmountException("transfer fail, need rollback");
        }
        if (needToThrow != null) throw needToThrow;
    }
}
