package service

import com.devexperts.account.Account
import com.devexperts.account.AccountKey
import com.devexperts.service.AccountServiceImpl
import spock.lang.Specification

class AccountServiceSpec extends Specification {

    def service = new AccountServiceImpl()

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
}