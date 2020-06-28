package com.devexperts.service.dao;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.data.repository.CrudRepository;

public interface AccountsDB extends CrudRepository<Account, AccountKey> {
}
