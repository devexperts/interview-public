package com.devexperts;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
            SpringApplication.run(ApplicationRunner.class, args);
    }

    @Bean
    AccountService accountService() {
        return new AccountServiceImpl();
    }
    

	@Bean
	@Profile("dev")
	public CommandLineRunner setUpAccounts(AccountServiceImpl accountService) {
		return (args) -> {
			System.out.println("Demo");

			int accountId1 = 1;
			AccountKey accountKey1 = AccountKey.valueOf(accountId1);
			Account account = new Account(accountKey1, "Joao", "Machado", 200000.00);
			
			int accountId2 = 2;
			AccountKey accountKey2 = AccountKey.valueOf(accountId2);
			Account account2 = new Account(accountKey2, "Miguel", "Machado", 200000.00);
			
			accountService.createAccount(account);
			accountService.createAccount(account2);
			
			System.out.println();

		};
	}
	
}
