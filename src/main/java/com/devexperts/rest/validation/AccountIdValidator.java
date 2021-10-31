package com.devexperts.rest.validation;

import com.devexperts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class AccountIdValidator implements ConstraintValidator<ExistingAccountId, Long> {

    private final AccountService accountService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if(id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id cannot be null");
        else if(id <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id must be above 0");
        else if(accountService.getAccount(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Account [%s] not found", id));
        else return true;
    }
}
