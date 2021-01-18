package com.devexperts.facade;

import com.devexperts.exception.DataNotFoundException;
import com.devexperts.exception.InputDataValidationException;
import com.devexperts.exception.InsufficientBalanceException;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pashkevich.ea
 */
@Component
public class AccountFacadeImpl implements AccountFacade {

    @Autowired
    private AccountService accountService;

    @Override
    public void transfer(long sourceId, long targetId, double amount) {
        if (sourceId == 0 || targetId == 0 || amount == 0) {
            throw new InputDataValidationException();
        }

        if (accountService.getAccount(sourceId) == null || accountService.getAccount(targetId) == null) {
            throw new DataNotFoundException();
        }

        if (accountService.getAccount(sourceId).getBalance() < amount) {
            throw new InsufficientBalanceException(accountService.getAccount(sourceId), amount);
        }

        accountService.transfer(accountService.getAccount(sourceId), accountService.getAccount(targetId), amount);
    }
}
