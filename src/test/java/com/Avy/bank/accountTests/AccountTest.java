package com.Avy.bank.accountTests;

import com.Avy.bank.dtos.requests.UserDepositRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountTest {

    @Test
    public void testThatAnAccountCanBeDepositedInto(){
        UserDepositRequest request = new UserDepositRequest();
        request.setAccountNumber("");

    }
}
