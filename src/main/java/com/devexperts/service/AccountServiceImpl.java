package com.devexperts.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accountsPer = new HashMap<AccountKey, Account>();
    
    @Override
    public void clear() {
        accountsPer.clear();
    }

    @Override
    public void createAccount(Account account) {
        accountsPer.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
    	return accountsPer.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if(amount <= 0 && source.getBalance().doubleValue() < amount) {
        	//throw exception
        }
        BigDecimal amountToTransfer =  new BigDecimal(amount);
        source.setBalance(source.getBalance().subtract(amountToTransfer)
        	.setScale(5, RoundingMode.HALF_EVEN));
        target.setBalance(target.getBalance().add(amountToTransfer)
        	.setScale(5, RoundingMode.HALF_EVEN));
        
    }
}
