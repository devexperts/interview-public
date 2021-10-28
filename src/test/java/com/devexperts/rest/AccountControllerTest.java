package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exceptions.AccountNotFoundException;
import com.devexperts.exceptions.AmountIsInvalidException;
import com.devexperts.exceptions.InsufficientAccountBalanceException;
import com.devexperts.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(accountController)
                .build();
    }

    /**
     * successful transfer
     * */
    @Test
    void transfer_0K() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/operations/transfer")
                        .param("sourceId", String.valueOf(1l))
                        .param("targetId", String.valueOf(2l))
                        .param("amount", String.valueOf(20.1)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * account is not found
     * */
    @Test
    void transfer_NOT_FOUND() throws Exception {
        doThrow(AccountNotFoundException.class).when(accountService)
                .transferWithChecks(1L, 2L, 20.1);
        MockHttpServletResponse response = mvc.perform(
                post("/api/operations/transfer")
                        .param("sourceId", String.valueOf(1l))
                        .param("targetId", String.valueOf(2l))
                        .param("amount", String.valueOf(20.1)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * amount is invalid
     * */
    @Test
    void transfer_BAD_REQUEST() throws Exception {
        doThrow(AmountIsInvalidException.class).when(accountService)
                .transferWithChecks(1L, 2L, -1.0);
        MockHttpServletResponse response = mvc.perform(
                post("/api/operations/transfer")
                        .param("sourceId", String.valueOf(1l))
                        .param("targetId", String.valueOf(2l))
                        .param("amount", String.valueOf(-1.0)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * one of the parameters in not present
     * */
    @Test
    void transfer_BAD_REQUEST2() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/operations/transfer")
                        .param("sourceId", String.valueOf(1l))
                        .param("amount", String.valueOf(1.0)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * insufficient account balance
     * */
    @Test
    void transfer_INTERNAL_SERVER_ERROR() throws Exception {
        doThrow(InsufficientAccountBalanceException.class).when(accountService)
                .transferWithChecks(1L, 2L, 1000.0);
        MockHttpServletResponse response = mvc.perform(
                post("/api/operations/transfer")
                        .param("sourceId", String.valueOf(1l))
                        .param("targetId", String.valueOf(2l))
                        .param("amount", String.valueOf(1000.0)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}