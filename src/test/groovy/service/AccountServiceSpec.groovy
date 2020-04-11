package service

import com.devexperts.account.Account
import com.devexperts.account.AccountKey
import com.devexperts.service.AccountServiceImpl
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AccountServiceSpec extends Specification {

    def service = new AccountServiceImpl()

    def cleanup() {
        service.clear()
    }

    def 'Creating accounts'() {
        given: 'two new accounts'
        def a1 = new Account(AccountKey.valueOf(1L), 'Boris', 'Borisov')
        def a2 = new Account(AccountKey.valueOf(2L), 'Bill', 'Gates')

        when: 'a request to create those accounts is made'
        [a1, a2].each { service.createAccount(it) }

        then: 'the accounts are created successfully'
        service.getAccount(1L) != null
        service.getAccount(2L) != null
    }

    def 'Transferring money when source account does not have enough money throws an exception'() {
        given: 'a source account with on money'
        def source = new Account(
                AccountKey.valueOf(1L),
                'Boris',
                'Borisov'
        )

        and: 'a target account'
        def target = new Account(
                AccountKey.valueOf(2L),
                'Bill',
                'Gates'
        )

        when: 'an attempt is made to transfer money from the source to the destination'
        service.transfer(source, target, Double.valueOf('100'))

        then: 'an exception should be thrown'
        thrown(RuntimeException)
    }

    def 'Transferring money when source account has enough money to transfer'() {
        given: 'a source account'
        def source = new Account(
                AccountKey.valueOf(1L),
                'Boris',
                'Borisov',
                Double.valueOf('200')
        )

        and: 'a target account'
        def target = new Account(
                AccountKey.valueOf(2L),
                'Bill',
                'Gates'
        )

        when: 'an attempt is made to transfer money from the source to the destination'
        service.transfer(source, target, Double.valueOf('100'))

        then: 'the transfer is successful'
        source.getBalance() == Double.valueOf('100')
        target.getBalance() == Double.valueOf('100')
    }

    def 'Transferring money in parallel'() {
        given: 'a source account'
        def source = new Account(
                AccountKey.valueOf(1L),
                'Boris',
                'Borisov',
                Double.valueOf('200')
        )

        and: 'a target account'
        def target = new Account(
                AccountKey.valueOf(2L),
                'Bill',
                'Gates'
        )

        and:
        def executionTimes = 3

        when:
        for (int i = 0; i < executionTimes; ++i) {
            ExecutorService executorService = Executors.newFixedThreadPool(10)
            for (int j = 0; j < 10; ++j) {
                executorService.execute(() -> {
                    service.transfer(source, target, Double.valueOf('1'))
                })
            }
            executorService.shutdown()
            executorService.awaitTermination(5, TimeUnit.SECONDS)
        }

        then: 'the correct amount of money is being transferred'
        source.getBalance() == Double.valueOf('170')
        target.getBalance() == Double.valueOf('30')
    }
}
