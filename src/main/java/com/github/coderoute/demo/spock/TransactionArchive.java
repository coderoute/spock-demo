package com.github.coderoute.demo.spock;

import java.math.BigDecimal;

public interface TransactionArchive {

    public String recordSuccessfulTransaction(String accountId, BigDecimal amount);

    public String recordFailedTransaction(String accountId, BigDecimal amount, String reason);
}
