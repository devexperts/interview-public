package main.java.com.devexperts;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static main.java.com.devexperts.TestUtils.createAccount;

public class AccountServiceTransferTests {
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
    }

    /**
     * Проверка корректности штатного перевода средств
     */
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

    /**
     * Проверка выбрасывания исключения для случая, когда сумма меревода имеет отрицательное значение
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTransferOnNegativeAmount() {
        Account firstAccount = createAccount(41);
        accountService.createAccount(firstAccount);
        Account secondAccount = createAccount(42);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, -100.0);
    }

    /**
     * Проверка выбрасывания исключения для случая, когда у клиента недостаточно средств на счёте
     */
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

    /**
     * Проверка перевода средств, когда на балансе отправителя {@code null средств}
     */
    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedSourceAccount() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", null);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", 1000.0);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    /**
     * Проверка перевода средств, когда на балансе получателя {@code null средств}
     */
    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedTargetAccount() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", 1000.0);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", null);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    /**
     * Проверка перевода средств, когда на балансах отправителя и отправителя {@code null средств}
     */
    @Test(expected = IllegalStateException.class)
    public void testTransferOnUninitializedAccounts() {
        Account firstAccount = new Account(AccountKey.valueOf(41), "FirstName", "LastName", null);
        accountService.createAccount(firstAccount);

        Account secondAccount = new Account(AccountKey.valueOf(43), "FirstName", "LastName", null);
        accountService.createAccount(secondAccount);

        accountService.transfer(firstAccount, secondAccount, 10.0);
    }

    /**
     * Проверка создания множества аккаунтов из разных потоков
     */
    @Test
    public void testMultiThreadAccountsCreation() throws InterruptedException {
        final int transfersCount = 50;
        CountDownLatch countDownLatch = new CountDownLatch(transfersCount);

        Account firstAccount = createAccount(41);
        accountService.createAccount(firstAccount);

        Account secondAccount = createAccount(42);
        accountService.createAccount(secondAccount);

        firstAccount.setBalance(5000.0);
        secondAccount.setBalance(0.0);

        Executor threadPoolExecutor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < transfersCount; i++) {
            threadPoolExecutor.execute(() -> {
                accountService.transfer(firstAccount, secondAccount, 10.0);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(Double.valueOf(4500.0), firstAccount.getBalance());
        Assert.assertEquals(Double.valueOf(500.0), secondAccount.getBalance());
    }
}
