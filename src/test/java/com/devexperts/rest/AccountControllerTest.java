package com.devexperts.rest;

import com.devexperts.IntegrationTest;
import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private final long firstAccountId = 1L;

    private final long secondAccountId = 2L;

    private final AccountKey firstAccountKey = AccountKey.valueOf(firstAccountId);

    private final AccountKey secondAccountKey = AccountKey.valueOf(secondAccountId);

    @Before
    public void setUp() {
        accountService.clear();
    }

    @Test
    public void shouldSuccessfullyTransferMoney() throws Exception {
        createAccount(firstAccountKey, 10.0);
        createAccount(secondAccountKey, 50.0);

        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "5"))
                .andExpect(status().isOk());

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(5, balance1);
        assertEquals(55, balance2);
    }

    @Test
    public void shouldReturnInternalServerErrorIfNotEnoughMoney() throws Exception {
        createAccount(firstAccountKey, 10.0);
        createAccount(secondAccountKey, 50.0);

        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "20"))
                .andExpect(status().isInternalServerError());

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(10, balance1);
        assertEquals(50, balance2);
    }

    @Test
    public void shouldReturnNotFoundIfSourceAccountNotExist() throws Exception {
        createAccount(firstAccountKey, 10.0);
        createAccount(secondAccountKey, 50.0);

        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "3")
                .param("targetId", "2")
                .param("amount", "5"))
                .andExpect(status().isNotFound());

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(10, balance1);
        assertEquals(50, balance2);
    }

    @Test
    public void shouldReturnNotFoundIfTargetAccountNotExist() throws Exception {
        createAccount(firstAccountKey, 10.0);
        createAccount(secondAccountKey, 50.0);

        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "3")
                .param("amount", "5"))
                .andExpect(status().isNotFound());

        Double balance1 = accountService.getAccount(firstAccountId).getBalance();
        Double balance2 = accountService.getAccount(secondAccountId).getBalance();
        assertEquals(10, balance1);
        assertEquals(50, balance2);
    }

    @Test
    public void shouldReturnBadRequestForEmptySourceId() throws Exception {
        mockMvc.perform(post("/api/operations/transfer")
                .param("targetId", "2")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForEmptyTargetId() throws Exception {
        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForEmptyAmount() throws Exception {
        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/operations/transfer")
                .param("sourceId", "1")
                .param("targetId", "2")
                .param("amount", "-1000"))
                .andExpect(status().isBadRequest());
    }

    private Account createAccount(AccountKey accountKey, Double balance) {
        Account account = new Account(accountKey, "Sherlock", "Holmes", balance);
        accountService.createAccount(account);
        return account;
    }

}