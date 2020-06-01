package com.devexperts.service.account;

import com.devexperts.entities.account.Account;
import com.devexperts.entities.account.AccountKey;
import com.devexperts.service.cache.CacheService;

// Вообще в жизне я не пишу столько интерфейсов на все случаи жизни.
// Да, вроде такой код выходит менее связным, но разобраться в нем бывает труднее из-за кучи лишних классов
public interface AccountCacheService extends CacheService<AccountKey, Account> {
}
