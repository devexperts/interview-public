package com.devexperts;

import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ApplicationRunner {
    public static void main(String[] args) {
            SpringApplication.run(ApplicationRunner.class, args);
    }

    @Bean
    AccountService accountService() {
        return new AccountServiceImpl();
    }
}
