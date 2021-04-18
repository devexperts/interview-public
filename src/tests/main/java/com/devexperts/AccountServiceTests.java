package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static main.java.com.devexperts.TestUtils.createAccount;

public class AccountServiceTests {
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
    }

    /**
     * Проверка создания одного аккаунта
     */
    @Test
    public void testSingleAccountCreation() {
        Account account = createAccount(42);
        accountService.createAccount(account);
        Account actual = accountService.getAccount(account.getAccountKey().getAccountId());
        Assert.assertEquals(account, actual);
    }

    /**
     * Проверка создания двух разных аккаунтов
     */
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

    /**
     * Проверка создания двух одинаковых аккаунтов
     * Ожидается, что повторное создание аккаунта недопустимо и будет выброшено исключение типа {@link IllegalArgumentException}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistingAccountCreation() {
        Account account = createAccount(42);
        accountService.createAccount(account);
        accountService.createAccount(account);
    }

    /**
     * Проверка удаления всех имеющихся аккаунтов
     */
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

    /**
     * Проверка создания нескольких аккаунтов из разных потоков
     *
     * @throws InterruptedException
     */
    @Test
    public void testMultiThreadAccountsCreation() throws InterruptedException {
        final int accountsCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(accountsCount);

        for (int i = 0; i < accountsCount; i++) {
            int accountId = i;
            new Thread(() -> {
                Account account = createAccount(accountId);
                accountService.createAccount(account);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await(10, TimeUnit.SECONDS);

        for (int i = 0; i < accountsCount; i++) {
            Account account = accountService.getAccount(i);
            Assert.assertNotNull(account);
            Assert.assertEquals(i, account.getAccountKey().getAccountId());
        }
    }
}
