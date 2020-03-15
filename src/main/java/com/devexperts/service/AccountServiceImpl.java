
package com.devexperts.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;

@Service
public class AccountServiceImpl implements AccountService {

	private final Map<AccountKey, Account> accountsPer = new HashMap<AccountKey, Account>();

	@Override
	public void clear() {
		accountsPer.clear();
	}

	@Override
	public void createAccount(Account account) {
		accountsPer.putIfAbsent(account.getAccountKey(), account);
	}

	@Override
	// this operation expose Account.update operation in concurrent env
	public Account getAccount(long id) {
		return accountsPer.get(AccountKey.valueOf(id));
	}

	@Override
	// since we can't change operations definitions, the method is synchronized to avoid deal lock
	// transfer (A, B) and  transfer (B, A)
	
	// we also need lock the objects to avoid concurrent updates
	public synchronized void transfer(Account source, Account target, double amount) {
		if (amount <= 0) {
			// throw exception
			throw new IllegalArgumentException("The amount to tranfer should be higher than zero");
		}

		synchronized (source) {
			Double sourceBalance = source.getBalance();

			if (sourceBalance.doubleValue() < amount) {
				throw new IllegalThreadStateException("No liquidity is available for the given source account");
			}

			synchronized (target) {
				BigDecimal bigAmount = BigDecimal.valueOf(amount).setScale(5, RoundingMode.HALF_EVEN);

				BigDecimal sourceBigInteger = new BigDecimal(source.getBalance());
				BigDecimal targetBigInteger = new BigDecimal(target.getBalance());

				source.setBalance(sourceBigInteger.subtract(bigAmount).setScale(5, RoundingMode.HALF_EVEN).doubleValue());
				target.setBalance(targetBigInteger.add(bigAmount).setScale(5, RoundingMode.HALF_EVEN).doubleValue());

			}
		}
	}
}
