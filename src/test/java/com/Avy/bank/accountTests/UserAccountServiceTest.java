package com.Avy.bank.accountTests;

import com.Avy.bank.dtos.requests.*;
import com.Avy.bank.dtos.responses.*;
import com.Avy.bank.exceptions.*;
import com.Avy.bank.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserAccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testAnExistingAccountCanBeLoggedInTo() throws UserNotFoundException {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setEmail("tobi4tee@gmail.com");
        request.setPassword("Agboola1234@.");

        AccountLoginResponse response = accountService.login(request);
        System.out.println(response);
        assertThat(response).isNotNull();

    }

    @Test
    public void testMultipleExistingAccountsCanBeLoggedInTo() throws UserNotFoundException {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setEmail("veraeze@gmail.com");
        request.setPassword("Veraeze1234@2.");

        AccountLoginResponse response = accountService.login(request);
        System.out.println(response);
        assertThat(response).isNotNull();

    }

    @Test
    public void testThatADepositTransactionCanBeExecutedOnAnExistingAccount() throws AccountNumberNotFound, InvalidAmountException, DescriptionException, TransactionException {
        UserDepositRequest request = new UserDepositRequest();

        request.setAccountName("Agboola Tobi Samuel");
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(7500));
        request.setDescription("School fees");
        request.setPerformedBy("Falade Adebola");
        request.setDepositDate("25/06/2024");

        UserDepositResponse response = accountService.makeDeposit(request);

        assertThat(response).isNotNull();
    }

    @Test
    public void testThatADepositTransactionCanBeExecutedOnAnExistingAccount2() throws AccountNumberNotFound, InvalidAmountException, DescriptionException, TransactionException {
        UserDepositRequest request = new UserDepositRequest();
        request.setAccountName("Vera Ezeagu");
        request.setAccountNumber("0000000028");
        request.setAmount(BigDecimal.valueOf(2500));
        request.setDescription("Feeding Allowance");
        request.setPerformedBy("Agboola Boluwatife");
        request.setDepositDate("27/06/2024");

        UserDepositResponse response = accountService.makeDeposit(request);

        assertThat(response).isNotNull();

    }

    @Test
    public void testThatMultipleDepositsCanBeMadeOnExistingAccount() throws AccountNumberNotFound, InvalidAmountException, DescriptionException, TransactionException {
        UserDepositRequest request = new UserDepositRequest();

        request.setAccountName("Agboola Tobi Samuel");
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(25000));
        request.setDescription("Feeding Allowance");
        request.setPerformedBy("Agboola Boluwatife");
        request.setDepositDate("27/06/2024");

        UserDepositResponse response = accountService.makeDeposit(request);

        assertThat(response).isNotNull();
    }

    @Test
    public void  testThatAnExistingAccountBalanceCanBeFound() throws AccountNumberNotFound, TransactionException {
        UserBalanceRequest request = new UserBalanceRequest();
        request.setAccountNumber("0000000028");

        UserBalanceResponse response = accountService.checkBalance(request);
        System.out.println(response.getBalance());
        assertThat(response).isNotNull();

    }

    @Test
    public void testThatWithdrawalCanBMadeOnAnExistingAccount() throws InvalidAmountException, AccountNumberNotFound, TransactionException {
        UserWithdrawRequest request = new UserWithdrawRequest();
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(500));
        request.setPerformedAt("25/06/2024");
        request.setDescription("Upkeep");
        request.setAccountName("Agboola Tobi Samuel");
        request.setPerformedBy("Agboola Tobi Samuel");


        UserWithdrawResponse response = accountService.makeWithdrawal(request);

        assertThat(response).isNotNull();

    }
    @Test
    public void testThatMultipleWithdrawalCanBePerformedOnAnExistingAccount() throws InvalidAmountException, AccountNumberNotFound, TransactionException {
        UserWithdrawRequest request = new UserWithdrawRequest();
        request.setAccountNumber("0000000028");
        request.setAmount(BigDecimal.valueOf(300));
        request.setPerformedBy("Agu Sandra");
        request.setDescription("Personal");
        request.setAccountName("Vera Ezeagu");
        request.setPerformedAt("25/06/2024");

        UserWithdrawResponse response = accountService.makeWithdrawal(request);

        assertThat(response).isNotNull();
    }

    @Test
    public void testThatFundTransferCanBeMadeBetweenTwoExistingAccounts() throws AccountNumberNotFound, InvalidAmountException, CustomException, TransactionException {
        UserFundTransferRequest request = new UserFundTransferRequest();
        request.setFromAccount("0000000037");
        request.setToAccount("0000000028");

        request.setAmount(BigDecimal.valueOf(750));
        request.setDescription("Sent for upkeep");
        request.setPerformedBy("Agboola Tobi Samuel");

        UserFundTransferResponse response = accountService.transferFund(request);
        assertThat(response).isNotNull();

    }

    @Test
    public void testThatAListOfTransactionsOnAnExistingAccountCanBeFound() throws AccountNumberNotFound, TransactionException {

        ViewTransactionHistory request = new ViewTransactionHistory();
        request.setAccountNumber("0000000037");

        ViewTransactionOnAccountResponse response = accountService.viewAllTransactions(request);
        System.out.println(response.getTransactionOnAccount());
        assertThat(response).isNotNull();
    }


    @Test
    public void testThatIfARegisteredAccountAttemptsToWithdrawANegativeAccountExceptionIsThrown(){
        UserWithdrawRequest request = new UserWithdrawRequest();
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(-4000));
        request.setPerformedAt("25/06/2024");
        request.setDescription("Upkeep");
        request.setAccountName("Agboola Tobi Samuel");
        request.setPerformedBy("Agboola Tobi Samuel");
        assertThrows(InvalidAmountException.class,()->accountService.makeWithdrawal(request));
    }

    @Test
    public void testThatIfARegisteredAccountAttemptsToTransferANegativeAmountExceptionIsThrown(){
        UserFundTransferRequest request = new UserFundTransferRequest();
        request.setFromAccount("0000000037");
        request.setToAccount("0000000028");

        request.setAmount(BigDecimal.valueOf(-500));
        request.setDescription("Sent for upkeep");
        request.setPerformedBy("Agu Sandra");

        assertThrows(InvalidAmountException.class,()->accountService.transferFund(request));
    }
    @Test
    public void testThatAttemptToDepositANegativeAmountThrowsException(){
        UserDepositRequest request = new UserDepositRequest();

        request.setAccountName("Agboola Tobi Samuel");
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(-500));
        request.setDescription("School fees");
        request.setPerformedBy("Falade Adebola");
        request.setDepositDate("25/06/2024");
        assertThrows(InvalidAmountException.class,()->accountService.makeDeposit(request));
    }


    @Test
    public void testAnExistingAccountCanBeLoggedOut() throws UserNotFoundException {
        AccountLogoutRequest request = new AccountLogoutRequest();
        request.setUserId(1L);
        AccountLogoutResponse response = accountService.logout(request);
        System.out.println(response);
        assertThat(response).isNotNull();
    }

    @Test
    public void testMultipleExistingAccount2CanBeLoggedOut() throws UserNotFoundException {
        AccountLogoutRequest request = new AccountLogoutRequest();
        request.setUserId(2L);
        AccountLogoutResponse response = accountService.logout(request);
        System.out.println(response);
        assertThat(response).isNotNull();
    }


    @Test
    public void testThatIfALoggedOutExistingAccountAttemptsToDepositExceptionIsThrown(){
        UserDepositRequest request = new UserDepositRequest();
        request.setAccountName("Agboola Tobi Samuel");
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(2500));
        request.setDescription("School fees");
        request.setPerformedBy("Falade Adebola");
        request.setDepositDate("25/06/2024");
        assertThrows(TransactionException.class,()->accountService.makeDeposit(request));

    }

    @Test
    public void testThatIfALoggedOutExistingAccountAttemptsToWithdrawExceptionIsThrown(){
        UserWithdrawRequest request = new UserWithdrawRequest();
        request.setAccountNumber("0000000037");
        request.setAmount(BigDecimal.valueOf(4000));
        request.setPerformedAt("25/06/2024");
        request.setDescription("Upkeep");
        request.setAccountName("Agboola Tobi Samuel");
        request.setPerformedBy("Agu Sandra");
        assertThrows(TransactionException.class,()->accountService.makeWithdrawal(request));

    }

    @Test
    public void testThatIfALoggedOutExistingAccountAttemptsToTransferExceptionIsThrown(){
        UserFundTransferRequest request = new UserFundTransferRequest();
        request.setFromAccount("0000000037");
        request.setToAccount("0000000028");

        request.setAmount(BigDecimal.valueOf(7500));
        request.setDescription("Sent for upkeep");
        request.setPerformedBy("Agu Sandra");
        assertThrows(TransactionException.class,()->accountService.transferFund(request));

    }

    @Test
    public void testThatIfALoggedOutExistingAccountAttemptsToCheckBalanceExceptionIsThrown(){
        UserBalanceRequest request = new UserBalanceRequest();
        request.setAccountNumber("0000000028");

        assertThrows(TransactionException.class,()->accountService.checkBalance(request));

    }









}
