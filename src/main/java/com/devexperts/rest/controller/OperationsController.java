package com.devexperts.rest.controller;

import com.devexperts.service.AccountService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
public class OperationsController {
    private final static Logger LOGGER = LoggerFactory.getLogger(OperationsController.class);

    private final AccountService accountService;

    @Autowired
    public OperationsController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "BAD_REQUEST"),
                    @ApiResponse(code = 404, message = "NOT_FOUND"),
                    @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")})
    @PostMapping("/transfer")
    public void transfer(@RequestParam long sourceId,
                         @RequestParam long targetId,
                         @RequestParam long amount) {
        LOGGER.trace("New incoming transfer request");

        this.accountService.transfer(sourceId, targetId, amount);
    }
}
