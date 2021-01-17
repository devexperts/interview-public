package com.devexperts.init;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pashkevich.ea
 */
@Component
public class Initialization {

    @SuppressWarnings("all")
    public Initialization(@Autowired AccountService accountService) {
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Ivan", "Petrov", 1000.0));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Sergey", "Lunin", 500.0));
        accountService.createAccount(new Account(AccountKey.valueOf(3), "Dmitriy", "Sidorov", 100.0));
        accountService.createAccount(new Account(AccountKey.valueOf(4), "Anton", "Rubin", 700.0));
        accountService.createAccount(new Account(AccountKey.valueOf(5), "Denis", "Denisov", 300.0));
    }
}
