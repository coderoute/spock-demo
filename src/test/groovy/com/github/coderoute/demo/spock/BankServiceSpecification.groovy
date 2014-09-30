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
        setup:

        // mocking (expected invocation) and stubbing (return value) has to be specified together

        // 1 * means expect 1 invocation of ..
        // Note that if cardinality is not specified,
        // the expectation is considered optional and this test will not fail when it should

        1 * bankAccountRepo.findAccount(TEST_ACCOUNT_ID) >> bankAccount
        def depositAmount = new BigDecimal("5.0")
        1 * transactionArchive.recordSuccessfulTransaction(TEST_ACCOUNT_ID, depositAmount) >> SUCCESS_TRANSACTION_ID

        when:
        def transactionId = underTest.deposit(TEST_ACCOUNT_ID, depositAmount)

        then:
        transactionId == SUCCESS_TRANSACTION_ID
    }

    def "Failed deposit attempt for all accounts is recorded in transaction archive"() {
        setup:
        1 * bankAccountRepo.findAccount(TEST_ACCOUNT_ID) >> bankAccount
        def depositAmount = new BigDecimal("5.0")
        1 * transactionArchive.recordFailedTransaction(TEST_ACCOUNT_ID, depositAmount) >> FAILED_TRANSACTION_ID
        def failureMessage = "Cannot update a non-active account";
        bankAccount.credit(depositAmount) >> { throw new TransactionException(failureMessage)}

        when:
        def transactionId = underTest.deposit(TEST_ACCOUNT_ID, depositAmount)

        then:
        transactionId == FAILED_TRANSACTION_ID
    }


}