package com.Avy.bank.userTests;

import com.Avy.bank.data.models.AccountType;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test

    public void testThatAUserCanRegister() throws UserExistException {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setFullName("Agboola Tobi Samuel");
        request.setEmail("tobi4tee@gmail.com");
        request.setPassword("password");
        request.setAddress("312, Sabo Yaba. Lagos");
        request.setPhoneNumber("08068952954");
        request.setAccountType(AccountType.SAVINGS);

        UserRegistrationResponse response = userService.register(request);
        assertThat(response).isNotNull();
    }

    @Test
    public void testThatMultipleUsersCanRegister() throws UserExistException {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setFullName("Agu Sandra");
        request.setEmail("agu.sandra@gmail.com");
        request.setPassword("agu1234Sandra");
        request.setAddress("312, Ikoyi. Lagos");
        request.setPhoneNumber("08165437288");
        request.setAccountType(AccountType.CURRENT);

        UserRegistrationResponse response = userService.register(request);
        assertThat(response).isNotNull();

    }

    @Test

    public void testThatARegisteredUserCannotRegister(){
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setFullName("Agu Sandra");
        request.setEmail("agu.sandra@gmail.com");
        request.setPassword("agu1234Sandra");
        request.setAddress("312, Ikoyi. Lagos");
        request.setPhoneNumber("08165437288");
        request.setAccountType(AccountType.CURRENT);

        assertThrows(UserExistException.class,()->userService.register(request));

    }

}
