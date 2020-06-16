package com.devexperts.configuration;

import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }
}
