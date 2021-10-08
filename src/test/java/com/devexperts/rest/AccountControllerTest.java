package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountController accountController;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void setUp() {
        Account account = new Account(AccountKey.valueOf(1), "Bob", "Benson", 100.0);
        Account account2 = new Account(AccountKey.valueOf(2), "John", "Benson", 100.0);

        accountService.createAccount(account);
        accountService.createAccount(account2);
    }

    @Test
    void transferWithValidParams() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "10");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(90,accountService.getAccount(1).getBalance());
        Assertions.assertEquals(110,accountService.getAccount(2).getBalance());
    }

    @Test
    void transferWithInvalidParams() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "xxxx")
                .param("amount", "xxxx");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "xxxx");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "")
                .param("targetId", "")
                .param("amount", "");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "xxxx")
                .param("targetId", "xxxx")
                .param("amount", "xxxx");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testWithNonExistAccount() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "3")
                .param("amount", "10");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "3")
                .param("targetId", "1")
                .param("amount", "10");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testWithLowBalance() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "110");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}