package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    //private final List<Account> accounts = new ArrayList<>();
    private Map<AccountKey, Account> accounts = new HashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        //accounts.add(account);
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        // переопределил метод equals в классе AccountKey
        // сложность метода O(N) где N размер списка аккаунтов
        /*return accounts.stream()
                .filter(account -> account.getAccountKey().equals(AccountKey.valueOf(id)))
                .findAny()
                .orElse(null);*/

        // если использовать HashMap вместо ArrayList, то сложность этого метода будет O(1)
        return accounts.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        //do nothing for now
    }
}
