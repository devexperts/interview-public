package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.bean.TransferHelper;
import com.devexperts.service.bean.TransferSideEnum;
import com.devexperts.exception.InsufficientBalanceException;
import org.springframework.stereotype.Service;

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
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    /* single thread method
    @Override
    public void transfer(Account source, Account target, double amount) {
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
    }
     */

    @Override
    public void transfer(Account source, Account target, double amount) {
        // avoid deadlock
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException(source, amount);
        }
        TransferHelper transferHelper = createTransferHelper(source, target);
        synchronized (transferHelper.getFirst()) {
            synchronized (transferHelper.getSecond()) {
                if (transferHelper.getFirstSide() == TransferSideEnum.FROM) {
                    transferHelper.getFirst().setBalance(transferHelper.getFirst().getBalance() - amount);
                    transferHelper.getSecond().setBalance(transferHelper.getSecond().getBalance() + amount);
                } else {
                    transferHelper.getFirst().setBalance(transferHelper.getFirst().getBalance() + amount);
                    transferHelper.getSecond().setBalance(transferHelper.getSecond().getBalance() - amount);
                }
            }
        }
    }

    private TransferHelper createTransferHelper(Account source, Account target) {
        int comparation = source.getAccountKey().compareTo(target.getAccountKey());
        if (comparation < 0) {
            return new TransferHelper(source, target, TransferSideEnum.FROM, TransferSideEnum.TO);
        } else if (comparation > 0) {
            return new TransferHelper(target, source, TransferSideEnum.TO, TransferSideEnum.FROM);
        } else {
            // == 0 than this is the same account, something wrong?
            throw new RuntimeException("transfer for same account");
        }
    }
}
