package com.devexperts.service;

import com.devexperts.account.Account;

public class TransferHelper {
    public static void validateBalance(Account source, Account target, double amount) throws RuntimeException{
        if(source.getBalance() < amount)
            throw new RuntimeException(String.format("[%s] has not enough money", source.getFirstName()));
        if(source.equals(target))
            throw new IllegalArgumentException("Cannot transfer to the same account");
    }
}
