package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;

    @Test
    void clear() {
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);

        accountService.createAccount(account);

        accountService.clear();

        Assertions.assertNull(accountService.getAccount(1));
    }

    @Test
    void createAccount() {
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);

        accountService.createAccount(account);

        Assertions.assertEquals(account,accountService.getAccount(1));
    }

    @Test
    void createAccountWithNull() {
        Assertions.assertThrows(NullPointerException.class,()->accountService.createAccount(null));
    }

    @Test
    void createAccountWithExistData(){
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);

        accountService.createAccount(account);

        Assertions.assertThrows(IllegalArgumentException.class,()->accountService.createAccount(account));
    }

    @Test
    void getAccount() {
        Account account = new Account(AccountKey.valueOf(2), "John", "Benson", 1000.0);

        accountService.createAccount(account);

        Assertions.assertEquals(account,accountService.getAccount(2));
        Assertions.assertNull(accountService.getAccount(3));

    }
}