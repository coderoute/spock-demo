package com.github.coderoute.demo.spock

import spock.lang.Specification
import spock.lang.Unroll

//@groovy.transform.TypeChecked
class BankAccountSpecification extends Specification {

    public static final String ACTIVE = "active"
    private static final String FROZEN = "frozen";
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

    @Unroll("Freezing account with initial state = #initialState")
    def "freezing account"() {
        setup:
        underTest.state = initialState

        when:
        underTest.freeze()

        then:
        underTest.state == FROZEN

        where: initialState << [ACTIVE, FROZEN]
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
}
