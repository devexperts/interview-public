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
}
