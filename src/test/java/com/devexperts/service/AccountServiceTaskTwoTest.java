package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.NegativeBalanceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for checking the second task")
class AccountServiceTaskTwoTest {
    private static Account source;
    private static Account target;
    private static AccountService accountService = new AccountServiceImpl();

    static {
        source = new Account(
                AccountKey.valueOf( 1123456 ), "Sergey", "Ivanov", 100d
        );

        target = new Account(
                AccountKey.valueOf( 12332343 ), "Vika", "Okulist", 0d
        );
    }

    @Test
    @DisplayName("Check 'transfer', normal work")
    void testTransfer() {
        double amount = 99.12d;
        double balanceTarget = target.getBalance();
        double balanceSource = source.getBalance();

        accountService.transfer( source, target, amount );

        assertEquals( target.getBalance(), balanceTarget + amount );
        assertEquals( source.getBalance(), balanceSource - amount );
    }

    @Test
    @DisplayName("Check 'transfer', that an 'NegativeBalanceException' was thrown")
    void testTransferThrowNegativeBalanceException() {
        double amount = 100.12d;
        assertThrows( NegativeBalanceException.class, () -> accountService.transfer( source, target, amount ) );
    }

    @Test
    @DisplayName(
            "Check 'transfer', that an 'IllegalArgumentException' was thrown, because 'source' or 'target' is null"
    )
    void testTransferSourceIsNull() {
        double amount = 100.12d;
        Throwable thrown = assertThrows( IllegalArgumentException.class, () ->
                accountService.transfer( null, target, amount )
        );

        assertEquals( thrown.getMessage(), "source or target is null" );
    }

    @Test
    @DisplayName(
            "Check 'transfer', that an 'IllegalArgumentException' was thrown, " +
                    "because 'source' and 'target' is one account"
    )
    void testTransferSourceAndTargetIsOneAccount() {
        double amount = 82.12d;
        Throwable thrown = assertThrows( IllegalArgumentException.class, () ->
                accountService.transfer( source, source, amount )
        );

        assertEquals( thrown.getMessage(), "source and target is one account" );
    }

    @Test
    @DisplayName(
            "Check 'transfer', that an 'IllegalArgumentException' was thrown, " +
                    "because 'source' and 'target' is one account"
    )
    void testTransferAmountIsNegativeOrZero() {
        double amountIsZero = 0.0d;
        double amountIsNegative = -0.1d;
        Throwable thrownIsZero = assertThrows( IllegalArgumentException.class, () ->
                accountService.transfer( source, target, amountIsZero )
        );
        assertEquals( thrownIsZero.getMessage(), "amount is negative or zero" );

        Throwable thrownIsNegative = assertThrows( IllegalArgumentException.class, () ->
                accountService.transfer( source, target, amountIsNegative )
        );
        assertEquals( thrownIsNegative.getMessage(), "amount is negative or zero" );
    }
}