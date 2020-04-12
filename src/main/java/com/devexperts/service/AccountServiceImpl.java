package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.exception.TransferException;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public void clear() {
        getAccounts().clear();
    }

    // Code review comment: assert account is non-null object.
    @Override
    public void createAccount(@NonNull Account account) {
        accounts.put(account.getAccountId(), account);
    }

    // Code review comment:
    // 1. Compare account key objects with appropriate equality method.
    // 2. Returning null should be avoided. Consider changing AccountService interface method getAccount to return
    // Optional<Account> instead. Since changing interface is not part of the task consider throwing
    // NoSuchElementException with appropriate error message.
    // 3. Performance of this method could be optimized by refactoring Account to not use the AccountKey class (which
    // could be deleted) but instead directly have accountId (type long) field inside and instead using List, using map
    // with key the accountId itself.
    @Override
    public Account getAccount(long id) {
        return Optional.ofNullable(accounts.get(id))
                .orElseThrow(() -> new NoSuchElementException("Account not found!"));
    }

    /**
     * {@inheritDoc}
     * Exception could occur if accounts are not registered in service {@code getAccount(source)} or
     * {@code getAccount(target)} throws exception, or accounts are the same {@code source.equals(target)}.
     */
    @Override
    public void transfer(Account source, Account target, double amount) {
        // Validate the accounts are available.
        getAccount(source.getAccountId());
        getAccount(target.getAccountId());

        // Validate not the same source and target.
        if (source.equals(target)) {
            throw new TransferException("Can't invoke transfer to the same account!");
        }

        // Lock the objects. Correct order of the locks should be assured for avoiding deadlock.
        final Account lock1 =
                source.getAccountId() < target.getAccountId() ? source : target;
        final Account lock2 =
                source.getAccountId() < target.getAccountId() ? target : source;

        // Calculate new balance.
        // Note: I assumed negative balances are possible (credit).
        synchronized (lock1) {
            synchronized (lock2) {
                source.setBalance(source.getBalance() - amount);
                target.setBalance(source.getBalance() + amount);
            }
        }
    }
}
