package com.github.coderoute.demo.spock

import spock.lang.Specification
import spock.lang.Unroll

//@groovy.transform.TypeChecked
class BankAccountSpecification extends Specification {

    public static final String ACTIVE = "active"
    private static final String FROZEN = "frozen"
    private static final String ACCOUNT_HOLDER_NAME = "Bob"

    private BankAccount underTest

    def setup() {
        underTest = new BankAccount(ACCOUNT_HOLDER_NAME)
    }

    def "account creation"() {
        when:
        underTest = new BankAccount("Charles")

        then:
        underTest.getBalance() == 0
        underTest.getName() == "Charles"
        underTest.getState() == ACTIVE
    }


    @Unroll("Unfreezing account with initial state = #initialState")
    def "Unfreezing account"() {
        setup:
        underTest.state = initialState

        when:
        underTest.unfreeze()

        then:
        underTest.state == expectedState

        where:
        initialState | expectedState
        ACTIVE       | ACTIVE
        FROZEN       | ACTIVE
    }

    @Unroll("Freezing account with initial state = #initialState")
    def "freezing account"() {
        setup:
        underTest.state = initialState

        when:
        underTest.freeze()

        then:
        underTest.state == FROZEN

        where:
        // the left shift operator << is also called a data pipe
        initialState << [ACTIVE, FROZEN]
        // a data table must have at least two columns, above can also be written as
        // initialState | _
        // ACTIVE       | _
        // FROZEN       | _
    }

    @Unroll("Add credit #creditAmount when initial balance=#initialBalance")
    def "Adding credit success"() {
        underTest.balance = initialBalance
        underTest.credit(creditAmount)

        expect:
        underTest.balance == expectedBalance

        where:
        initialBalance | creditAmount | expectedBalance
        0.0            | 0.0          | 0.0
        0.01           | -0.01        | 0.0
        1.11           | 0.01         | 1.12
        1.01           | -0.02        | 0.99
    }

    @Unroll("Add credit #creditAmount when initial balance=#initialBalance")
    def "Adding credit success with mock interaction"() {
        setup:
        underTest.balance = initialBalance

        when:
        underTest.credit(creditAmount)

        then:
        underTest.balance == expectedBalance

        where:
        initialBalance | creditAmount | expectedBalance
        0.0            | 0.0          | 0.0
        0.01           | -0.01        | 0.0
        1.11           | 0.01         | 1.12
        1.01           | -0.02        | 0.99
    }

    @Unroll("Add credit #creditAmount when initial balance=#initialBalance")
    def "Adding credit failure"() {
        setup:
        underTest.balance = initialBalance
        underTest.state = initialState

        when:
        underTest.credit(creditAmount)

        then:
        // Exception conditions are only allowed in 'then blocks', i.e. cannot use expect
        def e = thrown(exceptionType)
        e.message == expectedMessage

        where:
        initialBalance | initialState | creditAmount | exceptionType           | expectedMessage
        0.0            | ACTIVE       | -0.01        | TransactionException    | "Insufficient balance"
        1.11           | FROZEN       | 0.01         | TransactionException    | "Cannot update a non-active account"
    }
}
