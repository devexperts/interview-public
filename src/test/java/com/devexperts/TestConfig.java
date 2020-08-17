package com.devexperts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    @Bean
    public RestTemplate webClient() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
