package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.AccountNotFoundException;
import com.devexperts.service.exceptions.InsufficientBalanceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// Double is not recommended for financial operations.
// Change to BigDecimal
@Service
public class AccountServiceImpl implements AccountService {
    // change List to Map to improve the performance of the getAccount() method
    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account == null) {
            throw new NullPointerException("ACCOUNT EQUALS TO NULL.");
        }
        if (accounts.containsKey(account.getAccountKey())) {
            throw new IllegalArgumentException("ACCOUNT WITH SUCH A KEY ALREADY EXISTS.");
        }
        accounts.put(account.getAccountKey(),account);
    }

    @Override
    public Account getAccount(long id) {
        if (accounts.containsKey(AccountKey.valueOf(id))) {
            return accounts.get(AccountKey.valueOf(id));
        }
        return null;
    }
    @Override
    public void transfer(Account source, Account target, BigDecimal amount) {
        validateTransferParams(source,target,amount);

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
    }

    @Override
    public void validateTransferParams(Account source, Account target, BigDecimal amount) {
        if (source == null || target == null) {
            throw new AccountNotFoundException("SOURCE OR TARGET ACCOUNT DOES NOT EXIST.");
        }
        if (!accounts.containsKey(source.getAccountKey()) || !accounts.containsKey(target.getAccountKey())) {
            throw new AccountNotFoundException("SOURCE OR TARGET ACCOUNT DOES NOT EXIST.");
        }
        if (source == target) {
            throw new IllegalArgumentException("SOURCE AND TARGET ACCOUNTS CANNOT BE EQUAL.");
        }
        if (source.getBalance().compareTo(BigDecimal.ZERO) == 0 || source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("INSUFFICIENT SOURCE ACCOUNT BALANCE");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("AMOUNT CANNOT BE LESS THAN 0");
        }
    }
}
