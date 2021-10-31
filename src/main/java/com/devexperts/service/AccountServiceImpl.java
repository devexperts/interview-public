package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.devexperts.service.TransferHelper.validateBalance;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }


    @PostConstruct
    public void fillAccounts() {
        Account
        one = Account.of()
                .accountKey(AccountKey.valueOf(1))
                .balance(5d)
                .firstName("Daisy")
                .lastName("Duck")
                .build(),
        two = Account.of()
                .accountKey(AccountKey.valueOf(2))
                .balance(500000d)
                .firstName("Donald")
                .lastName("Duck")
                .build();

        accounts.put(one.getAccountKey(), one);
        accounts.put(two.getAccountKey(), two);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        List<Account> locks = Arrays.asList(source, target); //consider ids sequential
        locks.sort(Comparator.comparingLong(Account::getAccountId));

        synchronized (locks.get(0)) {
            synchronized (locks.get(1)) {
                validateBalance(source, target, amount);
                source.decreaseBalance(amount);
                target.increaseBalance(amount);
            }
        }
    }
}
