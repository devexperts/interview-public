package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exeptions.AccountAlreadyExistsException;
import com.devexperts.exeptions.NotEnoughFundsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(AccountKey accountKey, Account account) {
        if (accountKey == null || account == null){
            throw new IllegalArgumentException("Account or AccountKey is invalid");
        }
        if (accounts.putIfAbsent(accountKey, account) == null){
            throw new AccountAlreadyExistsException();
        }
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    /*@Override
    public void transfer(Account source, Account target, BigDecimal amount) {
        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
    }*/

    @Override
    public void transfer(final Account source, final Account target, final BigDecimal amount) {
        if (source == null || target == null || amount == null){
            throw new IllegalArgumentException("account or accountKey or amount is invalid");
        }
        if (source.getBalance().subtract(amount).compareTo(new BigDecimal("0")) < 0) {
            throw new NotEnoughFundsException();
        }
        final boolean prioritySourceTarget = source.getAccountKey().getAccountId() > target.getAccountKey().getAccountId();
        synchronized (prioritySourceTarget ? source : target) {
            synchronized (prioritySourceTarget ? target : source) {
                doTransfer(source, target, amount);
            }
        }
    }

    private void doTransfer(Account source, Account target, BigDecimal amount) {
        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
    }
}
