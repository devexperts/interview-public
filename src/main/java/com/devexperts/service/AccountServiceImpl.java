package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
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
    public void createAccount(Account account) {
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
    public Account getAccount(long id) {
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
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
