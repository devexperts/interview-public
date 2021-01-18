package com.devexperts.rest;

import com.devexperts.ApplicationRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author pashkevich.ea
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(
                post("/api/operations/transfer")
                        .param("source_id", "0")
                        .param("target_id", "0")
                        .param("amount", "0")).andExpect(status().isBadRequest());
    }

    @Test
    public void testAccountNotFoundRequest() throws Exception {
        mockMvc.perform(
                post("/api/operations/transfer")
                        .param("source_id", "1")
                        .param("target_id", "10")
                        .param("amount", "10")).andExpect(status().isNotFound());
    }

    @Test
    public void testAccountInsufficientBalanceRequest() throws Exception {
        mockMvc.perform(
                post("/api/operations/transfer")
                        .param("source_id", "1")
                        .param("target_id", "2")
                        .param("amount", "100000")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testOk() throws Exception {
        mockMvc.perform(
                post("/api/operations/transfer")
                        .param("source_id", "1")
                        .param("target_id", "2")
                        .param("amount", "10")).andExpect(status().isOk());
    }
}
