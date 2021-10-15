package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// TODO rewrite all null-checks using lombok library's annotaion @NonNull

@Service
public class AccountServiceImpl implements AccountService {

    private final List<Account> accounts = new ArrayList<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    /**
     * Add account into service's list
     *
     * @param account to add
     */
    @Override
    public void createAccount(Account account) {
        if (account != null)
            accounts.add(account);
        else {
            //throw new NullPointerException();
        }
    }

//    another version, with changed signature (so require more changes),
//    but this one checks, is such account has already added,
//    and return error code if such occurrence happens
//    another way of realization - create a custom exception AccountAlreadyExist and throw it

//    /**
//     * Add account into service's list
//     * @param account
//     * @return 0, if successfully added or 1, if such account already exist
//     */
//    public int createAccount(Account account) {
//        if (account != null) {
//            if (this.getAccount(account.getAccountKey()) == null) {
//                accounts.add(account);
//                return 0;
//            } else {
//                return 1;
//            }
//        } else {
//            //throw new NullPointerException();
//        }
//    }

    @Override
    public Account getAccount(long id) {
        return getAccount(AccountKey.valueOf(id));
    }

//    more general realization
//    if we will have way to translate AccountKey back to long - can be useless
//    but I didn't find such method
    public Account getAccount(AccountKey key) {
        if (key != null) {
            return accounts.parallelStream()
                    .filter(account -> account.getAccountKey().equals(key))
                    .findFirst()
                    .orElse(null);
        }
        else {
            //throw new NullPointerException();
            return null;
        }
    }

    // TASK 2

//    this method REQUIRE change of signature
//    if something happen inside (for example, source of transfer don't have enough money)
//    there is no way to find it quickly
//
//    in addition, I think, that throwing error is more suitable way here than error code
//    because problem is more severe and have parameters (how much money was not enough)

//    should this method allow transfer only between accounts that exist in collection "accounts"?
//    if it is correct - it should be stated somewhere
//    if it isn't correct - why we need List in this class?
//    I decide, that it is correct - otherwise, collection of this class will be useless
//    however, this means, that we can send to this method only AccountKeys of accounts instead of all Account object
//    but it also requires change of signature

    @Override
    public void transfer(Account source, Account target, double amount)
            throws IncorrectTargetOfTransfer, IncorrectAmountOfTransfer, InsufficientFundsException {
        if (amount <= 0.0) {
            throw new IncorrectAmountOfTransfer(amount);
        }

        if (source == null || target == null) {
            throw new NullPointerException();
        }

        if (source.getAccountKey().equals(target.getAccountKey())) {
            throw new IncorrectTargetOfTransfer(TypeOfIncorrection.SameAccount);
        }

        source = this.getAccount(source.getAccountKey());
        target = this.getAccount(target.getAccountKey());

          if (source == null || target == null)
              throw new IncorrectTargetOfTransfer(TypeOfIncorrection.NoSuchAccount);

        double moneyLeft = source.getBalance() - amount;
        if (moneyLeft >= 0) {
            source.setBalance(moneyLeft);
            target.setBalance(target.getBalance() + amount);
        } else {
            throw new InsufficientFundsException(-moneyLeft);
        }
    }

    // TASK 3

    public void transferThreadSafely(Account source, Account target, double amount)
            throws IncorrectTargetOfTransfer, NullPointerException, InsufficientFundsException, IncorrectAmountOfTransfer {

        if (amount <= 0.0) {
            throw new IncorrectAmountOfTransfer(amount);
        }

        if (source == null || target == null) {
            throw new NullPointerException();
        }

        if (source.getAccountKey().equals(target.getAccountKey())) {
            throw new IncorrectTargetOfTransfer(TypeOfIncorrection.SameAccount);
        }

        source = this.getAccount(source.getAccountKey());
        target = this.getAccount(target.getAccountKey());

        if (source == null || target == null)
            throw new IncorrectTargetOfTransfer(TypeOfIncorrection.NoSuchAccount);

        // getting the order of locking objects
        // source and target must be different accounts!
        Account willBeLockedFirst;
        Account willBeLockedSecond;

        if (source.getAccountKey().compare(target.getAccountKey()) > 0) {
            willBeLockedFirst = source;
            willBeLockedSecond = target;
        } else {
            willBeLockedFirst = target;
            willBeLockedSecond = source;
        }

        synchronized (willBeLockedFirst) {
            synchronized (willBeLockedSecond) {
                double moneyLeft = source.getBalance() - amount;
                if (moneyLeft >= 0) {
                    source.setBalance(moneyLeft);
                    target.setBalance(target.getBalance() + amount);
                } else {
                    throw new InsufficientFundsException(-moneyLeft);
                }
            }
        }

    }
}
