package com.transfer.app.account;

import com.transfer.app.exception.InsufficientBalanceException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountServiceTest {

    private final AccountService accountService = new AccountService();

    @Test
    public void should_fetch_accounts() {
        assertThat(accountService.getAllAccounts()).isNotEmpty();
    }

    @Test
    public void should_fetch_particular_account() {
        assertNotNull(accountService.getAccount("1"));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void should_throw_insufficent_balance_exception_when_no_required_balance() {
        // Our static map has 10000 in account 1 and 2000 in account 2
        accountService.performTransaction("1", "2", "15000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_illegal_argument_exception_when_acc_does_not_exist() {
        accountService.performTransaction("5", "2", "1500");
    }

    @Test
    public void should_update_account_balance_in_from_and_to_account_on_successful_transaction() {
        //On initialisation of map Account 1 has 10000 and 2 has 2000
        accountService.performTransaction("1", "2", "1000");
        assertEquals(accountService.getAccount("1").getAccountBalance(), 9000);
        assertEquals(accountService.getAccount("2").getAccountBalance(), 3000);
    }
}