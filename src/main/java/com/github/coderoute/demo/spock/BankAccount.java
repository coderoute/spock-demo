package com.github.coderoute.demo.spock;

import java.math.BigDecimal;

public class BankAccount {

    private static final String ACTIVE = "active";
    private static final String FROZEN = "frozen";

    private String name;
    private String state;
    private BigDecimal balance;

    public BankAccount(String name) {
        this.name = name;
        this.state = ACTIVE;
        this.balance = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void freeze() {
        this.state = FROZEN;
    }

    public void unfreeze() {
        this.state = ACTIVE;
    }

    public void credit(BigDecimal amount) {
        if (!this.state.equals(ACTIVE)) {
            throw new IllegalStateException("Cannot update a non-active account");
        }

        BigDecimal newBalance = balance.add(amount);
        if (newBalance.doubleValue() < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance = newBalance;
    }
}

