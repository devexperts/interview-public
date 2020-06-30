package com.devexperts.service.impl;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.dto.TransferMoneyDto;
import com.devexperts.exception.BadRequestException;
import com.devexperts.exception.NotFoundException;
import com.devexperts.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @Override
    public synchronized void transfer(TransferMoneyDto dto) {
        Account source = accounts.get(AccountKey.valueOf(dto.getSourceId()));
        Account target = accounts.get(AccountKey.valueOf(dto.getTargetId()));
        transfer(source, target, dto.getAmount());
    }

    public void transfer(Account source, Account target, double amount) {
        if(source == null) {
            throw new NotFoundException("The source account doesnt exist");
        }
        if(target == null) {
            throw new NotFoundException("The target account doesnt exist");
        }
        if(source.equals(target)) {
            throw new BadRequestException("Its pointless to transfer money between the same account");
        }
        if(amount < 0) {
            throw new BadRequestException("The amount can't be a negative value");
        }
        if(source.getBalance() < amount) {
            throw new BadRequestException("The source account doesnt have sufficient amount");
        }
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
    }
}
