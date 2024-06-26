package com.Avy.bank.userTests;

import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Test
    public void testThatAUserCanRegister(){
        UserRegistrationRequest request = new UserRegistrationRequest();

    }
}
