package com.devexperts.rest;

import com.devexperts.ApplicationRunner;
import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private AccountService accountService;
    private Account source;
    private Account target;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();

        source = new Account(AccountKey.valueOf(1), "IVAN", "IVANOV", new BigDecimal("100.0"));
        target = new Account(AccountKey.valueOf(2), "PETER", "PETROV", new BigDecimal("200.0"));
        accountService.createAccount(source);
        accountService.createAccount(target);
    }

    @Test
    public void shouldReturn200_ifTransferSuccessful() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId","1")
                .param("targetId","2")
                .param("amount","50.0");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(source.getBalance(), new BigDecimal("50.0"));
        Assert.assertEquals(target.getBalance(), new BigDecimal("250.0"));
    }

    @Test
    public void shouldReturn404_ifAccountNotFound() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "8")
                .param("targetId", "7")
                .param("amount", "50.0");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturn400_ifParamsInvalid() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "-50.0");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturn500_ifInsufficientBalance() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "1000.0");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
