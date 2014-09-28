package com.github.coderoute.demo.spock;

import java.math.BigDecimal;

public class BankAccount {

    private static final String ACTIVE = "active";
    private static final String FROZEN = "frozen";

    private String name;
    private String state;
    private double balance;

    public BankAccount(String name) {
        this.name = name;
        this.state = ACTIVE;
        this.balance = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public double getBalance() {
        return balance;
    }

    public void freeze() {
        this.state = FROZEN;
    }

    public void unfreeze() {
        this.state = ACTIVE;
    }

    public void credit(double amount) {
        if (!this.state.equals(ACTIVE)) {
            throw new IllegalStateException("Cannot update a non-active account");
        }
        if (balance + amount < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance += amount;
    }
}

