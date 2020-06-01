package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.cache.CacheServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class AccountCacheServiceImpl extends CacheServiceImpl<AccountKey, Account> implements AccountCacheService {

}
