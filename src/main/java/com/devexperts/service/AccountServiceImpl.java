package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.BalanceException;
import com.devexperts.validator.AccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    //TODO extract accounts to a repository layer so we could easily replace with a real DB implementation
    private final Map<AccountKey, Account> accounts;

    public AccountServiceImpl() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        AccountValidator.validateValidAccountEntity(account);
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        Account result = accounts.get(AccountKey.valueOf(id));
        AccountValidator.validateExistingAccount(id, result);
        return result;
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        executeTransfer(source, target, amount);
    }

    @Override
    public void transfer(long sourceId, long targetId, double amount) {
        var source = this.accounts.get(AccountKey.valueOf(sourceId));
        var target = this.accounts.get(AccountKey.valueOf(sourceId));

        AccountValidator.validateExistingAccount(sourceId, source);
        AccountValidator.validateExistingAccount(targetId, target);

        executeTransfer(source, target, amount);
    }

    /**
     * This method holds the logic about making the actual transfer.
     * Once we migrate to a real transfer operation we will implement a transaction logic in there.
     * @param source - account where we will get the money from
     * @param target - account where money will be deposited
     * @param amount - transfer amount
     */
    private void executeTransfer(Account source, Account target, double amount) {
        withdraw(source, amount);
        deposit(target, amount);
    }

    private void withdraw(Account account, double amount) {
        if(account.getBalance() < amount) {
            throw new BalanceException("Not sufficient resouces in source account");
        }

        LOGGER.trace("withdraw operation for {} with {} amount is triggered.", account.getAccountKey(), amount);

        account.setBalance(account.getBalance() - amount);
    }

    private void deposit(Account account, double amount){
        LOGGER.trace("deposit operation for {} with {} amount is triggered.", account.getAccountKey(), amount);
        account.setBalance(account.getBalance() + amount);
    }
}
