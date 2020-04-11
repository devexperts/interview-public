package service

import com.devexperts.account.Account
import com.devexperts.account.AccountKey
import com.devexperts.service.AccountServiceImpl
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AccountServiceTest extends Specification {

    def service = new AccountServiceImpl()

    def setup() {
        [new Account(
                AccountKey.valueOf(1L),
                'Boris',
                'Borisov',
                Double.valueOf('200')
        ),
         new Account(
                 AccountKey.valueOf(2L),
                 'Bill',
                 'Gates'
         )].each { service.createAccount(it) }
    }

    def cleanup() {
        service.clear()
    }

    def 'Transferring money when source account has enough money to transfer'() {
        when: 'an attempt is made to transfer money from the source to the destination'
        service.transfer(1L, 2L, Double.valueOf('100'))

        then: 'the transfer is successful'
        service.getAccount(1L).getBalance() == Double.valueOf('100')
        service.getAccount(2L).getBalance() == Double.valueOf('100')
    }

    def 'Transferring money in parallel'() {
        given:
        def executionTimes = 3

        when:
        for (int i = 0; i < executionTimes; ++i) {
            ExecutorService executorService = Executors.newFixedThreadPool(10)
            for (int j = 0; j < 10; ++j) {
                executorService.execute({
                    service.transfer(1L, 2L, Double.valueOf('1'))
                })
            }
            executorService.shutdown()
            executorService.awaitTermination(5, TimeUnit.SECONDS)
        }

        then: 'the correct amount of money is being transferred'
        service.getAccount(1L).getBalance() == Double.valueOf('170')
        service.getAccount(2L).getBalance() == Double.valueOf('30')
    }
}
