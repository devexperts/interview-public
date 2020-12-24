package com.devexperts.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devexperts.service.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

	@Autowired
	private AccountService accountService;

	@PutMapping("/options/transfer")
	public ResponseEntity<Void> transfer(@RequestParam("source_id") long sourceId,
			@RequestParam("target_id") long targetId, @RequestParam("amount") double amount) {

		accountService.transfer(accountService.getAccount(sourceId), accountService.getAccount(sourceId), amount);

		return ResponseEntity.ok().build();
	}

}
