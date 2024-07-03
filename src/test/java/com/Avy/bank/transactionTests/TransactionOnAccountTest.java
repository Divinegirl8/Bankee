//package com.Avy.bank.transactionTests;
//
//
//import com.Avy.bank.dtos.requests.ViewDepositRequest;
//import com.Avy.bank.dtos.responses.ViewDepositResponse;
//import com.Avy.bank.exceptions.AccountNumberNotFound;
//import com.Avy.bank.services.TransactionOnAccountService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class TransactionOnAccountTest {
//
//    @Autowired
//    private TransactionOnAccountService transactionOnAccountService;
//
//    @Test
//    public void testThatAListOfDePositTransactionByOnAnExistingAccountCanBeFound() throws AccountNumberNotFound {
//
//        ViewDepositRequest request = new ViewDepositRequest();
//        request.setAccountNumber("0000000019");
//
//        ViewDepositResponse response = transactionOnAccountService.viewDeposit(request);
//        System.out.println(response.getDeposits());
//        assertThat(response).isNotNull();
//
//    }
//
//}
