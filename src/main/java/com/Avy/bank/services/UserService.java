package com.Avy.bank.services;

import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.UserExistException;

public interface UserService {
    UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException;


}
