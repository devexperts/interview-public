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

    @Test
    void transferWithCorrectInfo(){
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);
        Account account2 = new Account(AccountKey.valueOf(2), "John", "Benson", 100.0);

        accountService.createAccount(account);
        accountService.createAccount(account2);

        accountService.transfer(account,account2,20.0);

        Assertions.assertEquals(80.0,account.getBalance());
        Assertions.assertEquals(120.0,account2.getBalance());
    }

    @Test
    void transferWithIncorrectInfo(){
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);
        Account account2 = new Account(AccountKey.valueOf(2), "John", "Benson", 100.0);
        Account account3 = new Account(AccountKey.valueOf(3), "Rob", "Benson", 100.0);

        accountService.createAccount(account);
        accountService.createAccount(account3);

        Assertions.assertThrows(NullPointerException.class,()->accountService.transfer(null,account2,20.0));
        Assertions.assertThrows(NullPointerException.class,()->accountService.transfer(account2,null,20.0));

        Assertions.assertThrows(IllegalArgumentException.class,()->accountService.transfer(account,account2,20.0));
        Assertions.assertThrows(IllegalArgumentException.class,()->accountService.transfer(account2,account,20.0));

        Assertions.assertThrows(IllegalArgumentException.class,()->accountService.transfer(account,account3,200.0));
    }




}