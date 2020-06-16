package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

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

    @Override
    public void transfer(Account source, Account target, double amount) {
        BigDecimal bDAmount = BigDecimal.valueOf(amount);
        double newSourceBalance =
                BigDecimal.valueOf(source.getBalance()).subtract(bDAmount).doubleValue();
        double newTargetBalance =
                BigDecimal.valueOf(target.getBalance()).add(bDAmount).doubleValue();
        if (isDoubleInfiniteOrNan(newSourceBalance) || isDoubleInfiniteOrNan(
                newTargetBalance)) {
            //TODO: some logging could be there
            //TODO: better to propagate checked exception to interface method
            throw new RuntimeException("Cannot transfer");
        }
        source.setBalance(newSourceBalance);
        target.setBalance(newTargetBalance);

        accounts.put(source.getAccountKey(), source);
        accounts.put(target.getAccountKey(), target);
    }

    private boolean isDoubleInfiniteOrNan(Double value) {
        return Double.isInfinite(value) || Double.isNaN(value);
    }
}
