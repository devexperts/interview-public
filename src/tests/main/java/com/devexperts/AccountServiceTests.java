package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static main.java.com.devexperts.TestUtils.createAccount;

public class AccountServiceTests {
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
    }

    @Test
    public void testSingleAccountCreation() {
        Account account = createAccount(42);
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertEquals(account, actual);
    }

    @Test
    public void testAccountsCreation() {
        Account firstAccount = createAccount(42);
        accountService.createAccount(firstAccount);
        Account actual = accountService.getAccount(firstAccount.getAccountKey().getAccountId());
        Assert.assertEquals(firstAccount, actual);

        Account otherAccount = createAccount(43);
        accountService.createAccount(otherAccount);
        actual = accountService.getAccount(otherAccount.getAccountKey().getAccountId());
        Assert.assertEquals(otherAccount, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistingAccountCreation() {
        Account account = createAccount(42);
        accountService.createAccount(account);
        accountService.createAccount(account);
    }

    @Test
    public void testAccountsClearing() {
        Account account = createAccount(42);
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNotNull(actual);

        accountService.clear();

        actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertNull(actual);
    }
}
