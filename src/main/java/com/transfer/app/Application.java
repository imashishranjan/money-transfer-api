package com.transfer.app;

import com.transfer.app.account.Account;
import com.transfer.app.account.AccountService;
import com.transfer.app.exception.InsufficientBalanceException;
import com.transfer.app.response.Response;

import static com.transfer.app.util.Utility.json;
import static com.transfer.app.util.Utility.toJson;
import static java.lang.String.format;
import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class Application {

    private static final AccountService accountService = new AccountService();


    public static void main(String[] args) {

        port(9200);

        get("/welcome", (request, response) -> "Welcome to money transfer");

        get("/accounts", (req, res) -> accountService.getAllAccounts(), json());

        get("/account/:accountNumber", (req, res) -> {
            String accountNumber = req.params(":accountNumber");
            Account account = accountService.getAccount(accountNumber);
            if (account != null) {
                return account;
            }
            res.status(400);
            return new Response("400", format("No account with account number %s found", accountNumber));
        }, json());

        post("/account/create", (request, response) -> {
            String name = request.queryParams("name");
            String amount = request.queryParams("balance");
            if (name == null || name.isEmpty() || amount == null || amount.isEmpty()){
                response.status(400);
                return new Response("400", format("Invalid name or amount for account creation"));
            }
            int accountNumber = accountService.createAccount(name, amount);
            response.status(201);
            return new Response("201", format("Account id created is %s", accountNumber));
        }, json());

        put("/account/withdraw/:fromAccountNumber", (req, res) -> {
            accountService.performTransaction(
                    req.params(":fromAccountNumber"),
                    req.queryParams("toAccountNumber"),
                    req.queryParams("amount")
            );
            res.status(200);
            return new Response("200", "Transaction successful");
        }, json());

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new Response("400", e)));
        });

        exception(InsufficientBalanceException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new Response("400", e)));
        });
    }
}
