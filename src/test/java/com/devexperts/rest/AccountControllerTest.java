package com.devexperts.rest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.exception.advice.EntityControllerExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

	private MockMvc mvc;

	@InjectMocks
	private AccountController accountController;

	@Mock
	private AccountService accountService;

	// These object will be magically initialized by the initFields method below.
//	private JacksonTester<List<SallesSell>> jsonSallesList;
	
	@Before
	public void setup() {
		// Initializes the JacksonTester
		JacksonTester.initFields(this, new ObjectMapper());

		// MockMvc standalone approach
		mvc = MockMvcBuilders.standaloneSetup(accountController)
				.setControllerAdvice(new EntityControllerExceptionHandler())
				.build();
	}
	
	@Test
	public void testHappyTransfer() throws Exception {
		
		long sourceId = 1;  
		Account source1 = new Account(AccountKey.valueOf(sourceId), "Joao", "Machado", Double.valueOf(20000));
		
		long targetId = 2; 
		Account source2 = new Account(AccountKey.valueOf(targetId), "Joao", "Miguel", Double.valueOf(10000));

		double amount = 2000;

		given(accountService.getAccount(sourceId)).willReturn(source1);
		given(accountService.getAccount(targetId)).willReturn(source2);
		
		//
		MockHttpServletResponse response = mvc
				.perform(post("/api/operations/transfer?sourceId=" + sourceId +"&targetId=" + targetId + "&amount=" + amount).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();
		
		
		then(response.getStatus())
			.as("Check is the transfer is completed")
			.isNotNull()
			.isEqualTo(HttpStatus.OK.value());

	}
	
	@Test
	@Ignore
	public void testFailedTransferIfSourceAccountDoesNotExist() throws Exception {
		

		long targetId = 2; 
		Account source2 = new Account(AccountKey.valueOf(targetId), "Joao", "Miguel", Double.valueOf(10000));

		double amount = 2000;

		given(accountService.getAccount(targetId)).willReturn(source2);
		
		//
		MockHttpServletResponse response = mvc
				.perform(post("/api/operations/transfer?sourceId=&targetId=" + targetId + "&amount=" + amount).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();
		
		
		then(response.getStatus())
			.as("Check is the transfer is completed")
			.isNotNull()
			.isEqualTo(HttpStatus.NOT_FOUND.value());

	}
	

}
