//package com.Avy.bank.transactionTests;
//
//
//import com.Avy.bank.dtos.requests.UserDepositRequest;
//import com.Avy.bank.dtos.responses.UserDepositResponse;
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
//    public void testThatADepositTransactionCanBeExecuted() {
//
//            UserDepositRequest request = new UserDepositRequest();
//
//
//            request.setAccountName("Agu Sandra");
//            request.setAccountNumber("0000000019");
//            request.setAmount(10000L);
//            request.setDescription("School fees");
//            request.setDepositor("Falade Adebola");
//            request.setDepositDate("25/06/2024");
//
//            UserDepositResponse response = transactionOnAccountService.makeDeposit(request);
//
//            assertThat(response).isNotNull();
//
//    }
//}
