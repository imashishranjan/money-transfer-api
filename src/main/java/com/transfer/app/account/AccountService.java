package com.transfer.app.account;

import com.transfer.app.exception.InsufficientBalanceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Long.valueOf;
import static java.lang.String.valueOf;

public class AccountService {

    private Map<String, Account> accounts = new HashMap<>(
            Map.of("1", new Account("1", "John", 10000), "2", new Account("2", "Rohan", 2000)
            ));

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void performTransaction(String fromAccountNumber, String toAccountNumber, String amount) {
        Account fromAccountDetails = accounts.get(fromAccountNumber);
        checkIfAccountExists(fromAccountDetails, "from", fromAccountNumber);
        Account toAccountDetails = accounts.get(toAccountNumber);
        checkIfAccountExists(toAccountDetails, "to", toAccountNumber);
        if (fromAccountDetails.getAccountBalance() < valueOf(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawl transaction");
        }
        updateBalance(fromAccountDetails, toAccountDetails, Long.valueOf(amount));
    }

    private void updateBalance(Account fromAccountDetails, Account toAccountDetails, long amount) {
        long fromAccountBalance = fromAccountDetails.getAccountBalance() - amount;
        long toAccountBalance = toAccountDetails.getAccountBalance() + amount;
        accounts.put(fromAccountDetails.getAccountNumber(), new Account(fromAccountDetails.getAccountNumber(), fromAccountDetails.getName(), fromAccountBalance));
        accounts.put(toAccountDetails.getAccountNumber(), new Account(toAccountDetails.getAccountNumber(), toAccountDetails.getName(), toAccountBalance));
    }

    private void checkIfAccountExists(Account account, String identifier, String accountNumber) {
        if (account == null) {
            throw new IllegalArgumentException(String.format("Invalid %s account number %s", identifier, accountNumber));
        }
    }

    public int createAccount(String name, String balance) {
        final Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);
        Account account = new Account(valueOf(id), name, valueOf(balance));
        accounts.put(valueOf(id), account);
        return id;
    }
}
