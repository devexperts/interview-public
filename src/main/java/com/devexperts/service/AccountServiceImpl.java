package com.devexperts.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

@Service
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
		// do nothing for now
	}
}
