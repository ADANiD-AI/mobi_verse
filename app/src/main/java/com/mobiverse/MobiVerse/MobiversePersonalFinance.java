package com.zai.mobiverse;

public class PersonalFinance {
    private double balance;

    public PersonalFinance() {
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
