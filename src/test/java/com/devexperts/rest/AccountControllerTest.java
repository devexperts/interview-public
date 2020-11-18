package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest{
  @Autowired
  private MockMvc mvc;

  @Autowired
  private AccountService accountService;

  @Test
  public void testPostTransfer_expect200() throws Exception {
    accountService.clear();
    Account a = new Account(AccountKey.valueOf(1), "a", "a", 100d);
    Account b = new Account(AccountKey.valueOf(2), "b", "b", 200d);
    accountService.createAccount(a);
    accountService.createAccount(b);

    mvc.perform(post("/api/operations/transfer?source_id=1&target_id=2&amount=5"))
      .andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  public void testPostTransfer_expect404() throws Exception {
    accountService.clear();
    Account a = new Account(AccountKey.valueOf(1), "a", "a", 100d);
    accountService.createAccount(a);

    mvc.perform(post("/api/operations/transfer?source_id=1&target_id=2&amount=5"))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  public void testPostTransfer_expect400() throws Exception {
    accountService.clear();
    Account a = new Account(AccountKey.valueOf(1), "a", "a", 100d);
    accountService.createAccount(a);

    mvc.perform(post("/api/operations/transfer?source_id=s&target_id=2&amount=as"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testPostTransfer_expect500() throws Exception {
    accountService.clear();
    Account a = new Account(AccountKey.valueOf(1), "a", "a", 100d);
    Account b = new Account(AccountKey.valueOf(2), "b", "b", 200d);
    accountService.createAccount(a);
    accountService.createAccount(b);

    mvc.perform(post("/api/operations/transfer?source_id=1&target_id=2&amount=500"))
      .andDo(print())
      .andExpect(status().isInternalServerError());
  }
}
