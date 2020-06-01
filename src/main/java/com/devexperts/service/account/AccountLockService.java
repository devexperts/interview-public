package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.lock.LockService;

public interface AccountLockService extends LockService<AccountKey, Account> {
}
