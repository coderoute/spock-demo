package com.github.coderoute.demo.spock

import spock.lang.Specification


class BankServiceSpecification extends Specification {

    def TEST_ACCOUNT_ID = "test-account-id"
    def SUCCESS_TRANSACTION_ID = "success-transaction-id"
    def FAILED_TRANSACTION_ID = "failed-transaction-id"

    private BankingService underTest
    private TransactionArchive transactionArchive
    private BankAccountRepo bankAccountRepo
    private FraudCheckingService fraudCheckingService
    private BankAccount bankAccount = Mock()

    def setup() {
        underTest = new BankingService()
        bankAccountRepo = Mock()
        transactionArchive = Mock()
        underTest.bankAccountRepo = bankAccountRepo
        underTest.transactionArchive = transactionArchive
    }

    def "Every successful deposit is recorded in transaction archive"() {
        def depositAmount = new BigDecimal("5.0")
        setup:

        // mocking (expected invocation) and stubbing (return value) has to be specified together

        // 1 * means expect 1 invocation of ..
        // Note that if cardinality is not specified,
        // the expectation is considered optional and this test will not fail when it should

        1 * bankAccountRepo.findAccount(TEST_ACCOUNT_ID) >> bankAccount
        1 * transactionArchive.recordSuccessfulTransaction(TEST_ACCOUNT_ID, depositAmount) >> SUCCESS_TRANSACTION_ID

        when:
        def transactionId = underTest.deposit(TEST_ACCOUNT_ID, depositAmount)

        then:
        // expect 0 invocation of failed transaction with any parameter list
        0 * transactionArchive.recordFailedTransaction(*_)
        transactionId == SUCCESS_TRANSACTION_ID
    }

    def "Failed deposit attempt for all accounts is recorded in transaction archive"() {
        def depositAmount = new BigDecimal("5.0")
        def failureMessage = "Cannot update a non-active account";
        setup:
        1 * bankAccountRepo.findAccount(TEST_ACCOUNT_ID) >> bankAccount
        1 * transactionArchive.recordFailedTransaction(TEST_ACCOUNT_ID, depositAmount, failureMessage) >> FAILED_TRANSACTION_ID
        bankAccount.credit(depositAmount) >> { throw new TransactionException(failureMessage)}

        when:
        def transactionId = underTest.deposit(TEST_ACCOUNT_ID, depositAmount)

        then:
        // expect 0 invocation of successful transaction with any value of parameters
        // (NOTE alternate notation for specific number of parameters)
        0 * transactionArchive.recordSuccessfulTransaction(_, _)
        transactionId == FAILED_TRANSACTION_ID
    }


}