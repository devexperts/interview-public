package main.java.com.devexperts;

import com.devexperts.ApplicationRunner;
import com.devexperts.account.Account;
import com.devexperts.rest.AccountController;
import com.devexperts.service.AccountService;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.regex.Pattern;

import static main.java.com.devexperts.TestUtils.createAccount;

/**
 * 200 (OK) - successful transfer
 * 400 (Bad Request) - one of the parameters in not present or amount is invalid
 * 404 (Not Found) - account is not found
 * 500 (Internal Server Error) - insufficient account balance
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService.clear();
    }

    /**
     * Test for 200 (OK) - successful transfer
     */
    @Test
    public void testForSuccessfulTransfer() throws Exception {
        // Предаврительно создаём аккаунты,
        // с которыми будет производиться работа через REST
        Account sourceAccount = createAccount(41, 1000);
        Account targetAccount = createAccount(42, 1000);
        accountService.createAccount(sourceAccount);
        accountService.createAccount(targetAccount);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/operations/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"source_id\": 41, \"target_id\": 42, \"amount\": 100.0 }");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(sourceAccount.getBalance(), Double.valueOf(900));
        Assert.assertEquals(targetAccount.getBalance(), Double.valueOf(1100));
    }

    /**
     * Test for 400 (Bad Request) - one of the parameters in not present or amount is invalid
     */
    @Test
    public void testForBadRequest() throws Exception {
        // Предаврительно создаём аккаунты,
        // с которыми будет производиться работа через REST
        accountService.createAccount(createAccount(1, 1000));
        accountService.createAccount(createAccount(2, 1000));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/operations/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"source_id\": 1, \"target_id\": 2, \"amount\": -100.0 }");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test for 404 (Not Found) - account is not found
     */
    @Test
    public void testForAccountNotFound() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/operations/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"source_id\": 1, \"target_id\": 2, \"amount\": -100.0 }");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test for 500 (Internal Server Error) - insufficient account balance
     */
    @Test
    public void testForInsufficientAccountBalance() throws Exception {
        // Предаврительно создаём аккаунты,
        // с которыми будет производиться работа через REST.
        // Для первого аккаунта устанавливаем недостаточное кол-во средств
        accountService.createAccount(createAccount(1, 10));
        accountService.createAccount(createAccount(2, 1000));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/operations/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"source_id\": 1, \"target_id\": 2, \"amount\": 100.0 }");

        mockMvc.perform(builder)
                .andExpect(result -> {
                    Pattern pattern = Pattern.compile(".*insufficient.*", Pattern.CASE_INSENSITIVE);
                    String body = result.getResponse().getContentAsString();
                    MatcherAssert.assertThat(
                            "Response body doesn`t contain message about insufficient balance.", pattern.matcher(body).matches());
                })
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
