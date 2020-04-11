package controller

import com.devexperts.ApplicationRunner
import com.devexperts.account.Account
import com.devexperts.account.AccountKey
import com.devexperts.service.AccountService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(classes = ApplicationRunner)
class AccountControllerIntegrationTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Autowired
    AccountService accountService

    MockMvc mockMvc

    def setup() {
        mockMvc = webAppContextSetup(context).build()
    }

    def 'Transferring money from source to target account'() {
        given: 'two existing accounts in the system'
        [new Account(
                AccountKey.valueOf(1L),
                'Boris',
                'Borisov',
                Double.valueOf(sourceBalance)
        ),
         new Account(
                 AccountKey.valueOf(2L),
                 'Bill',
                 'Gates',
                 Double.valueOf(targetBalance)
         )].each { accountService.createAccount(it) }

        and: 'a transfer request'
        def transfer = JsonOutput.toJson([
                sourceId: sourceAccountId,
                targetId: 2L,
                amount  : Double.valueOf(transferAmount)
        ])

        when: 'a request to transfer money from target to source account is made'
        def response = mockMvc.perform(post('/api/transfer')
                .content(transfer)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andReturn()
                .response

        then: 'it is successful'
        response.status == expectedHttpStatusCode

        where:
        sourceAccountId | sourceBalance | targetBalance | transferAmount | expectedHttpStatusCode
        1L              | '200'         | '0'           | '100'          | 200
        50L             | '200'         | '0'           | '100'          | 404
        1L              | '200'         | '0'           | '300'          | 500
        1L              | '200'         | '0'           | '0'            | 400
    }

}
