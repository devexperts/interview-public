package com.devexperts.service;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.service.exceptions.InsufficientFundsException;
import com.devexperts.service.exceptions.IncorrectTargetOfTransfer;
import com.devexperts.service.exceptions.TypeOfIncorrection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class AccountServiceImplTransferTest {

    AccountServiceImpl accountService;

    @BeforeEach
    void loadDataToAccountService() {
        accountService = new AccountServiceImpl();
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Ivan", "Ivanov", 100.0));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Petr", "Petrov", 200.0));
        accountService.createAccount(new Account(AccountKey.valueOf(3), "Pavel", "Pavlov", 300.0));
        accountService.createAccount(new Account(AccountKey.valueOf(4), "Ivan", "Ivanov", 0.0));
    }

    @Test
    void testTransferCorrect() {
        try {
            accountService.transfer(accountService.getAccount(1), accountService.getAccount(2), 50);
            Assertions.assertEquals(50.0, accountService.getAccount(1).getBalance());
            Assertions.assertEquals(250.0, accountService.getAccount(2).getBalance());
            accountService.transfer(accountService.getAccount(1), accountService.getAccount(3), 50);
            Assertions.assertEquals(0.0, accountService.getAccount(1).getBalance());
            Assertions.assertEquals(350.0, accountService.getAccount(3).getBalance());
        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferNullAccount() {
        try {
             accountService.transfer(accountService.getAccount(1), accountService.getAccount(5), 50);
        } catch (NullPointerException ex) {

        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferIncorrectTargetOfTransfer() {
        try {
            try {
                Account account = new Account(AccountKey.valueOf(5), "Masha", "Mashova", 0.0);
                accountService.transfer(account, accountService.getAccount(1), 50);
            } catch (IncorrectTargetOfTransfer ex) {
                Assertions.assertEquals(TypeOfIncorrection.NoSuchAccount.toString(), ex.getTypeOfIncorrection());
            }

            try {
                accountService.transfer(accountService.getAccount(1), accountService.getAccount(1), 50);
            } catch (IncorrectTargetOfTransfer ex) {
                Assertions.assertEquals(TypeOfIncorrection.SameAccount.toString(), ex.getTypeOfIncorrection());
            }
        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferInsufficientFunds() {
        try {
            accountService.transferThreadSafely(accountService.getAccount(1), accountService.getAccount(3), 200);
            Assertions.fail();
        } catch (InsufficientFundsException ex) {

        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferThreadSafelyCorrect() throws InterruptedException {
        // first test
        Sender senderFirstToSecond = new Sender(accountService.getAccount(1), accountService.getAccount(2), 1, 50);

        senderFirstToSecond.start();

        senderFirstToSecond.join();

        Assertions.assertEquals(50.0, accountService.getAccount(1).getBalance());
        Assertions.assertEquals(250.0, accountService.getAccount(2).getBalance());

        // second test
        senderFirstToSecond = new Sender(accountService.getAccount(1), accountService.getAccount(2), 1, 50);
        Sender senderSecondToFirst = new Sender(accountService.getAccount(2), accountService.getAccount(1), 2, 50);

        senderFirstToSecond.start();
        senderSecondToFirst.start();

        senderFirstToSecond.join();
        senderSecondToFirst.join();

        Assertions.assertEquals(100.0, accountService.getAccount(1).getBalance());
        Assertions.assertEquals(200.0, accountService.getAccount(2).getBalance());

        // third test
        senderFirstToSecond = new Sender(accountService.getAccount(1), accountService.getAccount(2), 1, 50);
        Sender senderSecondToThird = new Sender(accountService.getAccount(2), accountService.getAccount(3), 4, 25);
        Sender senderThirdToFirst = new Sender(accountService.getAccount(3), accountService.getAccount(1), 5, 40);

        senderFirstToSecond.start();
        senderSecondToThird.start();
        senderThirdToFirst.start();

        senderFirstToSecond.join();
        senderSecondToThird.join();
        senderThirdToFirst.join();

        Assertions.assertEquals(250.0, accountService.getAccount(1).getBalance());
        Assertions.assertEquals(150.0, accountService.getAccount(2).getBalance());
        Assertions.assertEquals(200.0, accountService.getAccount(3).getBalance());
    }

    @Test
    void testTransferThreadSafelyNullAccount() {
        try {
             accountService.transferThreadSafely(accountService.getAccount(1), accountService.getAccount(5), 50);
        } catch (NullPointerException ex) {

        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferThreadSafelyIncorrectTargetOfTransfer() {
        try {
            try {
                Account account = new Account(AccountKey.valueOf(5), "Masha", "Mashova", 0.0);
                accountService.transferThreadSafely(account, accountService.getAccount(1), 50);
            } catch (IncorrectTargetOfTransfer ex) {
                Assertions.assertEquals(TypeOfIncorrection.NoSuchAccount.toString(), ex.getTypeOfIncorrection());
            }

            try {
                accountService.transferThreadSafely(accountService.getAccount(1), accountService.getAccount(1), 50);
            } catch (IncorrectTargetOfTransfer ex) {
                Assertions.assertEquals(TypeOfIncorrection.SameAccount.toString(), ex.getTypeOfIncorrection());
            }
        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void testTransferThreadSafelyInsufficientFunds() {
        try {
            accountService.transferThreadSafely(accountService.getAccount(1), accountService.getAccount(3), 200);
            Assertions.fail();
        } catch (InsufficientFundsException ex) {

        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    class Sender extends Thread {
        Account source;
        Account target;
        double amount;
        int times;

        Sender(Account source, Account target, double amount, int times) {
            this.source = source;
            this.target = target;
            this.amount = amount;
            this.times = times;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < times; i++) {
                    accountService.transferThreadSafely(source, target, amount);
                }
            } catch (Exception ex) {

            }
        }
    }
}
