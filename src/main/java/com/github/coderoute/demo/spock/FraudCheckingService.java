package com.github.coderoute.demo.spock;

import java.math.BigDecimal;

public interface FraudCheckingService {

    public void processTransaction(String accountId, BigDecimal amount);
}
