package com.Avy.bank.accountTests;

import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.exceptions.AccountNumberNotFound;
import com.Avy.bank.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testThatAnAccountCanBeDepositedInto() throws AccountNumberNotFound {
        UserDepositRequest request = new UserDepositRequest();
        request.setAccountNumber("0000000019");
        request.setAmount(10000L);
        request.setDescription("School fees");
        request.setDepositor("Falade Adebola");
        request.setDepositDate(LocalDateTime.parse("25/06/2024"));

        UserDepositResponse response = accountService.deposit(request);

        assertThat(response).isNotNull();

    }
}
