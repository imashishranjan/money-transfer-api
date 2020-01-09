package com.transfer.app.account;

public class Account {
    private String accountNumber;
    private String name;
    private long accountBalance;

    public Account(String accountNumber, String name, long accountBalance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.accountBalance = accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public long getAccountBalance() {
        return accountBalance;
    }
}
