package com.devexperts.service;

import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

	@InjectMocks
	private AccountServiceImpl accountService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAccountsService() {
		//given two accounts in the service 
		int accountId1 = 1;
		AccountKey accountKey1 = AccountKey.valueOf(accountId1);
		Account account = new Account(accountKey1, "Joao", "Machado", 200000.00);

		accountService.createAccount(account);
		
		//when clear the account service 
		Account account1Actual =  accountService.getAccount(accountId1);
		
		//then any accounts should exist

		then(account1Actual)
			.as("Account should be null")
			.isEqualTo(account1Actual);
		
	}
	
	@Test
	public void testCreateAccount() {
		//given an accounts created in the service 
		int accountId1 = 1;
		AccountKey accountKey1 = AccountKey.valueOf(accountId1);
		Account account = new Account(accountKey1, "Joao", "Machado", 200000.00);
		
		//when clear the account service 
		accountService.createAccount(account);
		
		
		//then any accounts should exist
		Account account1Actual =  accountService.getAccount(accountId1);


		then(account1Actual)
			.as("Account is not correct")
			.isEqualTo(account);
		
	}
	
	
	@Test
	public void testClearAccountsService() {
		//given two accounts in the service 
		int accountId1 = 1;
		AccountKey accountKey1 = AccountKey.valueOf(accountId1);
		Account account = new Account(accountKey1, "Joao", "Machado", 200000.00);
		
		int accountId2 = 2;
		AccountKey accountKey2 = AccountKey.valueOf(accountId2);
		Account account2 = new Account(accountKey2, "Miguel", "Machado", 200000.00);
		
		accountService.createAccount(account);
		accountService.createAccount(account2);
		
		//when clear the account service 
		accountService.clear();
		
		
		//then any accounts should exist
		Account account1Actual =  accountService.getAccount(accountId1);
		Account account2Actual = accountService.getAccount(accountId2);


		then(account1Actual)
			.as("Account should be null")
			.isNull();
		
		then(account2Actual)
			.as("Account should be null")
			.isNull();
		
	}
	
	@Test
	public void testTransferService() {
		//given two accounts in the service 
		int accountId1 = 1;
		AccountKey accountKey1 = AccountKey.valueOf(accountId1);
		Account account = new Account(accountKey1, "Joao", "Machado", 200000.00);
		
		int accountId2 = 2;
		AccountKey accountKey2 = AccountKey.valueOf(accountId2);
		Account account2 = new Account(accountKey2, "Miguel", "Machado", 200000.00);
		
		accountService.createAccount(account);
		accountService.createAccount(account2);
		
		//when clear the account service 
		accountService.transfer(account, account2, 200000.00);
		
		
		//then any accounts should exist
		then(account.getBalance())
			.as("Account Balence should be 0.00000")
			.isEqualTo(0.0);
		
		then(account2.getBalance())
			.as("Account Balence should be 400000.00000")
			.isEqualTo(400000.0);
		
	}
}
