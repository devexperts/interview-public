package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static main.java.com.devexperts.TestUtils.createAccount;

public class AccountServiceTransferTests {
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
    }

    @Test
    public void testTransfer() {
        Account firstAccount = createAccount(41);
        accountService.createAccount(firstAccount);

        Account secondAccount = createAccount(42);
        accountService.createAccount(secondAccount);

        firstAccount.setBalance(500.0);
        secondAccount.setBalance(0.0);
        accountService.transfer(firstAccount, secondAccount, 100.0);

        Assert.assertEquals(Double.valueOf(400.0), firstAccount.getBalance());
        Assert.assertEquals(Double.valueOf(100.0), secondAccount.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferOnNegativeAmount() {
        Account firstAccount = createAccount(41);
        accountService.createAccount(firstAccount);
        Account secondAccount = createAccount(42);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, -100.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedSourceAccount() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", null);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", 1000.0);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedTargetAccount() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", 1000.0);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", null);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedAccounts() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", null);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", null);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testTransferOnInsufficientFunds() {
        Account firstAccount = createAccount(41);
        accountService.createAccount(firstAccount);
        firstAccount.setBalance(0.0);

        Account secondAccount = createAccount(42);
        accountService.createAccount(secondAccount);
        secondAccount.setBalance(0.0);

        accountService.transfer(firstAccount, secondAccount, 100.0);
    }
}
