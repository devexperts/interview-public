package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private final String URL_TEMPLATE = "/api/operations/transfer";

    @BeforeEach
    public void createAccounts() {
        accountService.createAccount(
                new Account(AccountKey.valueOf(1), "John", "Statham", (double) 10000000));
        accountService.createAccount(
                new Account(AccountKey.valueOf(2), "Domenik", "Toretto", (double) 0));
    }

    @AfterEach
    public void deleteAccounts() {
        accountService.clear();
    }

    @Test
    void transferFromOneAccountToAnotherThenStatusIsOK() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(URL_TEMPLATE)
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "1337"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void transferFromOneAccountToAnotherWhenAmountIsInvalidThenStatusIsBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(URL_TEMPLATE)
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "-228"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferFromOneAccountToAnotherWhenAccountNotExistThenStatusIsNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(URL_TEMPLATE)
                .param("sourceId", "1")
                .param("targetId", "3")
                .param("amount", "1337"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void transferFromOneAccountToAnotherWhenInsufficientAccountBalanceThenStatusIsInternalServerError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(URL_TEMPLATE)
                .param("sourceId", "2")
                .param("targetId", "1")
                .param("amount", "1337"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}