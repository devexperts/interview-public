package com.devexperts.validator;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.EntityNotFoundException;
import com.devexperts.exception.InvalidAccountException;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountValidatorTest {

    @Test(expected = EntityNotFoundException.class)
    public void test_validateExistingAccount_shouldThrowException_nullAccount() {
        AccountValidator.validateExistingAccount(0, null);
    }

    @Test
    public void test_validateExistingAccount_shouldBeOk_NotNullAccount() {
        AccountValidator.validateExistingAccount(0, mock(Account.class));
    }

    @Test(expected = InvalidAccountException.class)
    public void test_validateValidAccountEntity_shouldThrowException_nullAccount() {
        AccountValidator.validateValidAccountEntity(null);
    }

    @Test(expected = InvalidAccountException.class)
    public void test_validateValidAccountEntity_shouldThrowException_nullAccountKey() {
        var mockAccount = mock(Account.class);
        when(mockAccount.getAccountKey()).thenReturn(null);

        AccountValidator.validateValidAccountEntity(mockAccount);
    }

    @Test
    public void test_validateValidAccountEntity_shouldBeOk_validAccount() {
        var mockAccount = mock(Account.class);
        when(mockAccount.getAccountKey()).thenReturn(AccountKey.valueOf(1));

        AccountValidator.validateValidAccountEntity(mockAccount);
    }
}