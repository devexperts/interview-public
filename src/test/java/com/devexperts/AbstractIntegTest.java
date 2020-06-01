package com.devexperts;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AbstractIntegTest.TestConfiguration.class})
public abstract class AbstractIntegTest {


    @Configuration
    @ComponentScan("com.devexperts")
    public static class TestConfiguration {

    }
}
