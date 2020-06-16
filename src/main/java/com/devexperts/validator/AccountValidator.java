package com.devexperts.validator;

import com.devexperts.account.Account;
import com.devexperts.exception.EntityNotFoundException;
import com.devexperts.exception.InvalidAccountException;

public final class AccountValidator {

    private AccountValidator(){}

    public static void validateExistingAccount(long searchedId, Account account) {
        if(account == null) {
            throw new EntityNotFoundException(String.format("Account with id %d was not found", searchedId));
        }
    }

    public static void validateValidAccountEntity(Account account) {
        if(account == null) {
            throw new InvalidAccountException("Account DTO should not be empty");
        } else if(account.getAccountKey() == null) {
            throw new InvalidAccountException("Account DTO should contain a valid unique identifier");
        }
    }
}
