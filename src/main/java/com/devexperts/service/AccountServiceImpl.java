package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.AmountIsInvalidException;
import com.devexperts.exceptions.InsufficientAccountBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (!accounts.containsKey(account.getAccountKey())) {
            accounts.put(account.getAccountKey(), account);
        }
    }

    @Override
    public Account getAccount(long id) {
        synchronized (this) {
            return accounts.get(AccountKey.valueOf(id));
        }
    }

    private void transfer(Account source, Account target, double amount) {
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
    }

    @Override
    @Transactional
    public void transferWithChecks(long sourceId, long targetId, double amount)
            throws AccountNotFoundException, InsufficientAccountBalanceException,
            AmountIsInvalidException {

        if (amount <= 0) {
            throw new AmountIsInvalidException("Amount is invalid");
        }

        Account sourceAccount = getAccount(sourceId);
        Account targetAccount = getAccount(targetId);

        if (sourceAccount == null) {
            throw new AccountNotFoundException("Source account is not found.");
        }

        if (targetAccount == null) {
            throw new AccountNotFoundException("Target account is not found.");
        }
        if (sourceId < targetId) {
            synchronized (sourceAccount) {
                synchronized (targetAccount) {
                    if (sourceAccount.getBalance() < amount) {
                        throw new InsufficientAccountBalanceException("Insufficient account balance");
                    }
                    transfer(sourceAccount, targetAccount, amount);
                }
            }
        } else {
            synchronized (targetAccount) {
                synchronized (sourceAccount) {
                    if (sourceAccount.getBalance() < amount) {
                        throw new InsufficientAccountBalanceException("Insufficient account balance");
                    }
                    transfer(sourceAccount, targetAccount, amount);
                }
            }
        }
    }
}
