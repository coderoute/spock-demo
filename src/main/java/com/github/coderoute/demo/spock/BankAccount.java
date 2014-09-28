package com.github.coderoute.demo.spock;

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
}
