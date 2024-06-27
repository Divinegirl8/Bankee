package com.Avy.bank.services;

import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) {
        boolean isRegistered = user
    }
}
