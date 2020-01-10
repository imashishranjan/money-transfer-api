# money-transfer-api
Simple money transfer api using Java Spark. This is a very basic and simple demo to show basic apis for money tansfer with POC using Spark-Java framework.

## Tech used
- Java 11
- Spark for creating api
- Database : For simplicity HashMap of Java is used as a data store for application


## Build
mvn clean install

## Run the app
- run `java -jar money-transfer-1.0-SNAPSHOT.jar` from target directory
- application runs on port 9200


## Overfiew of endpoints
This app assumes the database is a Hashmap which is present in service class. It gets initialised with two
accounts with number 1 and 2.

- Welcome  `http://localhost:9200/welcome` (GET)
- Get all accounts  `http://localhost:9200/accounts` (GET)
- Get particular accounts - `http://localhost:9200/account/{accountId}` (GET)
- Create account - `localhost:9200/account/create?name={Name}&balance={Balance}` (POST) 
- Transfer - `localhost:9200/account/withdraw/{fromAccount}?toAccountNumber={toAccount}&amount={Amount}`(PUT)



### Extra info
- There are various validation in place for appropriate response from app.
ex- Bad request when account does not exist, invalid params, insufficent balance in transaction.
- All above success and validation failure scenarios are covered by test case.
