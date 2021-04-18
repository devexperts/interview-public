package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccountServiceTests {
    private AccountService accountService;
    private Account account;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
        account = new Account(AccountKey.valueOf(42), "FirstName", "LastName", 0.0);
    }

    @Test
    public void testSingleAccountCreation() {
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertEquals(account, actual);
    }

    @Test
    public void testAccountsCreation() {
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertEquals(account, actual);

        Account otherAccount = new Account(AccountKey.valueOf(account.getAccountKey().getAccountId() + 1), "FirstName", "LastName", 0.0);
        accountService.createAccount(otherAccount);
        actual = accountService.getAccount(otherAccount.getAccountKey().getAccountId());
        Assert.assertEquals(otherAccount, actual);
        Assert.assertNotEquals(account, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistingAccountCreation() {
        Account account = new Account(AccountKey.valueOf(1), "FirstName", "LastName", 0.0);
        accountService.createAccount(account);
        accountService.createAccount(account);
    }

    @Test
    public void testAccountsClearing() {
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNotNull(actual);

        accountService.clear();

        actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNull(actual);
    }
}
