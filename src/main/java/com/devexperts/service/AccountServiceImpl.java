package com.devexperts.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.AccountNotRegisteredException;
import com.devexperts.exception.ParametersInvalidException;
import com.devexperts.exception.NotEnoughAmountException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
	private final Map<AccountKey, Account> allAccounts = new HashMap<>();
	
	@Override
	public void clear() {
		allAccounts.clear();
	}

	@Override
	public void createAccount(Account account) {
		allAccounts.put(account.getAccountKey(), account);
	}

	@Override
	public Account getAccount(long id) {
		return allAccounts.get(AccountKey.valueOf(id));
	}

	@Override
	public void transfer(Account source, Account target, double amount) {
		synchronized(this) {
			validateAndExecuteTransfer(source, target, amount);
		}
	}
	
	private void validateAndExecuteTransfer(Account source, Account target, double amount) {
		
		if(!(isAccountCreated(source) && isAccountCreated(target))) {
			log.error("Cannot procede with transfer, one of the accounts is not registered");
			throw new AccountNotRegisteredException("Cannot procede with transfer, one of the accounts is not registered");
		}
		
		if(checkParams(source) && checkParams(target)) {
			log.error("Invalid parameters");
			throw new ParametersInvalidException("Invalid parameters");
		}
		
		if(source.getBalance() < amount) {
			log.error(String.format("% as balance is more than the balance of the sourse", amount));
			throw new NotEnoughAmountException(String.format("% as balance is more than the balance of the sourse", amount));
		}
		allAccounts.get(source.getAccountKey()).decreaseBalance(amount);
		allAccounts.get(target.getAccountKey()).increaseBalance(amount);
	}
	
	private boolean isAccountCreated(Account account) {
		return allAccounts.containsKey(account.getAccountKey());
	}
	
	private boolean checkParams(Account account) {
		return account.getBalance() < 0 && account.getFirstName().isEmpty() && account.getLastName().isEmpty();
	}
}
