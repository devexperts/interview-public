package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pashkevich.ea
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Ivan", "Petrov", 1000.0));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Sergey", "Lunin", 500.0));
        accountService.createAccount(new Account(AccountKey.valueOf(3), "Dmitriy", "Sidorov", 100.0));
        accountService.createAccount(new Account(AccountKey.valueOf(4), "Anton", "Rubin", 700.0));
        accountService.createAccount(new Account(AccountKey.valueOf(5), "Denis", "Denisov", 300.0));
    }

    @AfterEach()
    public void clean() {
        accountService.clear();
    }

    @Test
    public void testGetById() {
        Account account = accountService.getAccount(4);
        assertEquals(account.getFirstName(), "Anton");
    }
}
