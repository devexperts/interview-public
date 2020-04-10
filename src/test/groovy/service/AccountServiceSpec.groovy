package service

import spock.lang.Specification

class AccountServiceSpec extends Specification {

    def 'Test should work'() {
        given:
        def a = 3
        def b = 4

        when:
        def result = a + b

        then:
        result == 7
    }
}
