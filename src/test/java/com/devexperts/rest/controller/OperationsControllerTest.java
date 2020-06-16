package com.devexperts.rest.controller;

import com.devexperts.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperationsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService mockAccountService;

    @Test
    public void test_transfer_shouldReturnSuccess() throws Exception {
        String path = "/api/operations/";

        doNothing().when(mockAccountService).transfer(any(Long.class), any(Long.class), any(Double.class));

        this.mockMvc.perform(post(path + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .requestAttr("sourceId", 1)
                .requestAttr("targetId",2)
                .requestAttr("amount", 500))
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());
    }
}
