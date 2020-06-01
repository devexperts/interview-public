package com.devexperts.rest.account;

import com.devexperts.AbstractIntegTest;
import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.account.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.devexperts.service.account.AccountTestUtils.createAccount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerIntegTest extends AbstractIntegTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountService accountService;

    private final String TRANSFER_ENDPOINT = "/api/transfer";
    private final String SOURCE_ID_PARAM = "sourceId";
    private final String TARGET_ID_PARAM = "targetId";
    private final String AMOUNT_PARAM = "amount";

    private Long ACC1_ID = 1L;
    private Long ACC2_ID = 2L;

    @Before
    public void before() {
        accountService.clear();
        accountService.createAccount(createAccount(ACC1_ID, new BigDecimal("100")));
        accountService.createAccount(createAccount(ACC2_ID, new BigDecimal("100")));
    }


    @Test
    public void testWithMissedParams_responseBadRequest() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(TARGET_ID_PARAM, "2"))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(AMOUNT_PARAM, "2"))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "") //
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "")) //
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithWrongParams_responseBadRequest() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "aaa") //
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "0"))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "0")) //
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "abc")) //
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "-1") //
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isBadRequest());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "1")
                .param(TARGET_ID_PARAM, "2")
                .param(AMOUNT_PARAM, "-100")) //
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithSameAccounts_responseBadRequest() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                .param(TARGET_ID_PARAM, ACC1_ID.toString())
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testTransferForNonExistedAccount_responseNotFound() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                .param(TARGET_ID_PARAM, "123")
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isNotFound());

        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, "123")
                .param(TARGET_ID_PARAM, ACC2_ID.toString())
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testTransferWithInsufficientBalance_response500() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                .param(TARGET_ID_PARAM, ACC2_ID.toString())
                .param(AMOUNT_PARAM, "9999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testTransferFullAmount_responseSuccess() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                .param(TARGET_ID_PARAM, ACC2_ID.toString())
                .param(AMOUNT_PARAM, "100"))
                .andExpect(status().isOk());

        Account acc1 = accountService.getAccount(AccountKey.valueOf(ACC1_ID));
        Account acc2 = accountService.getAccount(AccountKey.valueOf(ACC2_ID));

        assertTrue(equals(BigDecimal.ZERO, acc1.getBalance()));
        assertTrue(equals(new BigDecimal("200"), acc2.getBalance()));
    }

    @Test
    public void testTransferHalfAmount_responseSuccess() throws Exception {
        mvc.perform(post(TRANSFER_ENDPOINT)
                .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                .param(TARGET_ID_PARAM, ACC2_ID.toString())
                .param(AMOUNT_PARAM, "50.05"))
                .andExpect(status().isOk());

        Account acc1 = accountService.getAccount(AccountKey.valueOf(ACC1_ID));
        Account acc2 = accountService.getAccount(AccountKey.valueOf(ACC2_ID));

        assertTrue(equals(new BigDecimal("49.95"), acc1.getBalance()));
        assertTrue(equals(new BigDecimal("150.05"), acc2.getBalance()));
    }

    @Test
    public void testConcurrentTransfer_responseSuccess() throws Exception {
        AtomicInteger errCount = new AtomicInteger();
        int itCount = 1_000;
        int thCount = 5;
        BigDecimal transferAmount = new BigDecimal("0.01");

        BigDecimal totalTransferred = transferAmount.multiply(new BigDecimal(itCount * thCount));
        assertTrue(totalTransferred.compareTo(new BigDecimal("100")) <= 0);

        ExecutorService executor = Executors.newFixedThreadPool(thCount);

        Runnable task = () -> {
            try {
                for (int i = 0; i < itCount; i++) {
                    mvc.perform(post(TRANSFER_ENDPOINT)
                            .param(SOURCE_ID_PARAM, ACC1_ID.toString())
                            .param(TARGET_ID_PARAM, ACC2_ID.toString())
                            .param(AMOUNT_PARAM, transferAmount.toPlainString()))
                            .andExpect(status().isOk());
                }
            } catch (Exception e) {
                errCount.incrementAndGet();
            }
        };

        for (int i = 0; i < thCount; i++) {
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(30L, TimeUnit.SECONDS);

        Account acc1 = accountService.getAccount(AccountKey.valueOf(ACC1_ID));
        Account acc2 = accountService.getAccount(AccountKey.valueOf(ACC2_ID));

        assertEquals(0, errCount.get());
        assertTrue(equals(new BigDecimal("100").subtract(totalTransferred), acc1.getBalance()));
        assertTrue(equals(new BigDecimal("100").add(totalTransferred), acc2.getBalance()));
    }


    private boolean equals(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }
}
