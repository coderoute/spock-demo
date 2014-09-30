package com.github.coderoute.demo.spock;

import java.math.BigDecimal;

public class BankingService {

    private BankAccountRepo bankAccountRepo;
    private TransactionArchive transactionArchive;

    public String deposit(String accountId, BigDecimal amount) {

        BankAccount bankAccount = bankAccountRepo.findAccount(accountId);
        bankAccount.credit(amount);
        return transactionArchive.recordSuccessfulTransaction(accountId, amount);

    }
}
