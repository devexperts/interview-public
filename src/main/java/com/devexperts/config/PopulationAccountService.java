package com.devexperts.config;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopulationAccountService {
    @Bean
    CommandLineRunner initClientDatabase(AccountService accountService) {
        return args -> {
            accountService.createAccount(new Account(AccountKey.valueOf(1L), "Ivan", "Ivanov", 500.0));
            accountService.createAccount(new Account(AccountKey.valueOf(2L), "Petr", "Petrov", 350.0));
            accountService.createAccount(new Account(AccountKey.valueOf(3L), "Igor", "Sidorov", 750.0));
        };
    }
}
