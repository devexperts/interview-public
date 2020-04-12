package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientAccountBalanceException;
import com.devexperts.exception.InvalidBalanceException;
import com.devexperts.exception.InvalidTransferAccountException;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.DecimalMin;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Code review comment: add Javadoc for description of the implementation.

/**
 * Basic implementation for {@link AccountService} methods.
 */
@Service
public class AccountServiceImpl implements AccountService {

    // Code review comment: access accounts through getter for further extensibility of this class.
    // Example - in a child class of this, get accounts could be changed to different kind of collections.
    @Getter
    private final Map<Long, Account> accounts = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        getAccounts().clear();
    }

    // Code review comment: assert account is non-null object.

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAccount(@NonNull Account account) {
        getAccounts().put(account.getAccountId(), account);
    }

    // Code review comment:
    // 1. Compare account key objects with appropriate equality method.
    // 2. Returning null should be avoided. Consider changing AccountService interface method getAccount to return
    // Optional<Account> instead. Since changing interface is not part of the task consider throwing
    // NoSuchElementException with appropriate error message.
    // 3. Performance of this method could be optimized by refactoring Account to not use the AccountKey class (which
    // could be deleted) but instead directly have accountId (type long) field inside and instead using List, using map
    // with key the accountId itself.

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getAccount(long id) {
        return getAccountIfFound(id).orElseThrow(() -> new AccountNotFoundException("Account not found!"));
    }

    /**
     * {@inheritDoc}
     * Exception could occur if accounts are not registered in service {@code getAccount(source)} or
     * {@code getAccount(target)} throws exception, or accounts are the same {@code source.equals(target)}.
     */
    @Override
    public void transfer(Account source, Account target, @DecimalMin(value = "0", inclusive = false) double amount) {
        // Validate the accounts are available.
        validateAccountExistForId(source.getAccountId());
        validateAccountExistForId(target.getAccountId());

        // Validate not the same source and target.
        validateTransferSourceAndTarget(source, target);

        // Lock the objects. Correct order of the locks should be assured for avoiding deadlock.
        final Account lock1 =
                source.getAccountId() < target.getAccountId() ? source : target;
        final Account lock2 =
                source.getAccountId() < target.getAccountId() ? target : source;

        // Calculate new balance.
        synchronized (lock1) {
            synchronized (lock2) {
                withdraw(source, amount);
                deposit(target, amount);
            }
        }
    }

    private Optional<Account> getAccountIfFound(long id) {
        return Optional.ofNullable(getAccounts().get(id));
    }

    private void validateTransferSourceAndTarget(Account source, Account target) {
        if (source.equals(target)) {
            throw new InvalidTransferAccountException("Can't invoke transfer to the same account!");
        }
    }

    private void validateAccountExistForId(long id) {
        getAccount(id);
    }

    private void withdraw(Account account, double amount) {
        double calculatedBalance = account.getBalance() - amount;
        if (calculatedBalance > 0) {
            account.setBalance(calculatedBalance);
        } else {
            throw new InsufficientAccountBalanceException("Account doesn't have enough funds for withdraw.");
        }
    }

    private void deposit(Account account, double amount) {
        double calculatedBalance = account.getBalance() + amount;
        if (calculatedBalance > 0) {
            account.setBalance(calculatedBalance);
        } else {
            throw new InvalidBalanceException("Deposit failed. Invalid balance after deposit.");
        }
    }
}
