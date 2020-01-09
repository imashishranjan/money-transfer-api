package com.transfer.app;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApplicationTest {

    @ClassRule
    public static SparkServer<ApplicationSparkTest> testServer = new SparkServer<>(ApplicationTest.ApplicationSparkTest.class, 9200);

    @Test
    public void test_app_welcome_uri_loads() throws Exception {
        GetMethod get = testServer.get("/welcome", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals("Welcome to money transfer", new String(httpResponse.body()));
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_fetch_accounts() throws Exception {
        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("\"accountNumber\":\"1\"");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_fetch_particular_account() throws Exception {
        GetMethod get = testServer.get("/account/2", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("\"accountNumber\":\"2\"");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_throw_bad_request_for_account_not_found() throws Exception {
        GetMethod get = testServer.get("/account/5", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("No account with account number 5 found");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_create_account_for_valid_request() throws Exception {
        PostMethod post = testServer.post("/account/create?name=Johny&balance=100", "", false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(201, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Account id created");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_give_bad_request_if_invalid_request() throws Exception {
        PostMethod post = testServer.post("/account/create?name&balance=100", "", false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(400, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Invalid name or amount for account creation");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_transfer_balance_between_accounts() throws Exception {
        PutMethod put = testServer.put("/account/withdraw/1?toAccountNumber=2&amount=1000","", false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(200, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Transaction successful");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_send_bad_request_if_insufficient_balance() throws Exception {
        PutMethod put = testServer.put("/account/withdraw/1?toAccountNumber=2&amount=100000","", false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(400, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Insufficient balance for withdrawl transaction");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_send_bad_request_if_invalid_from_account_in_transaction() throws Exception {
        PutMethod put = testServer.put("/account/withdraw/78?toAccountNumber=2&amount=100000","", false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(400, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Invalid from account number 78");
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void should_send_bad_request_if_invalid_to_account_in_transaction() throws Exception {
        PutMethod put = testServer.put("/account/withdraw/1?toAccountNumber=8&amount=100000","", false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(400, httpResponse.code());
        assertThat(new String(httpResponse.body())).contains("Invalid to account number 8");
        assertNotNull(testServer.getApplication());
    }

    public static class ApplicationSparkTest implements SparkApplication {
        @Override
        public void init() {
            Application.main(new String[0]);
        }
    }
}