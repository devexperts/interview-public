package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.AmountIsInvalidException;
import com.devexperts.exceptions.InsufficientAccountBalanceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    AccountService accountService;

    @Test
    void transfer() {
        Account ac1 = new Account(AccountKey.valueOf(101L), "Abdusame", "Ochil-zoda", 100.00);
        Account ac2 = new Account(AccountKey.valueOf(201L), "Alex", "Ivanov", 33.00);
        accountService.transfer(ac1, ac2, 19.22);

        assertEquals(100.00 - 19.22, ac1.getBalance());
        assertEquals(33.00 + 19.22, ac2.getBalance());
        accountService.clear();
    }

    @Test
    void transfer_OK() throws AccountNotFoundException, AmountIsInvalidException, InsufficientAccountBalanceException {
        accountService.createAccount(new Account(AccountKey.valueOf(1L),
                "Bill", "Gates", 222.00));
        accountService.createAccount(new Account(AccountKey.valueOf(2L),
                "Steve", "Jobs", 133.00));
        accountService.transferWithChecks(1L, 2L, 10);
        assertEquals(222.00 - 10, accountService.getAccount(1L).getBalance());
        assertEquals(133.00 + 10, accountService.getAccount(2L).getBalance());
        accountService.clear();
    }


    @Test
    void transfer_AmountIsInvalid() {
        accountService.createAccount(new Account(AccountKey.valueOf(1L),
                "Bill", "Gates", 222.00));
        accountService.createAccount(new Account(AccountKey.valueOf(2L),
                "Steve", "Jobs", 133.00));
        assertThrows(AmountIsInvalidException.class, () ->
                accountService.transferWithChecks(1L, 2L, 0)
        );
        accountService.clear();
    }

    @Test
    void transfer_AccountIsNotFound() {
        assertThrows(AccountNotFoundException.class, () ->
                accountService.transferWithChecks(33L, 22L, 10)
        );
    }

    @Test
    void transfer_InsufficientAccountBalance() {
        accountService.createAccount(new Account(AccountKey.valueOf(1L),
                "Bill", "Gates", 222.00));
        accountService.createAccount(new Account(AccountKey.valueOf(2L),
                "Steve", "Jobs", 133.00));
        assertThrows(InsufficientAccountBalanceException.class, () ->
            accountService.transferWithChecks(1L, 2L, 1000)
        );
        accountService.clear();
    }
}