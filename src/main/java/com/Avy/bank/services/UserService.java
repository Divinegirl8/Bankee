package com.Avy.bank.services;

import com.Avy.bank.dtos.requests.UserLoginRequest;
import com.Avy.bank.dtos.requests.UserLogoutRequest;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserLoginResponse;
import com.Avy.bank.dtos.responses.UserLogoutResponse;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.InvalidLoginCredentails;
import com.Avy.bank.exceptions.InvalidRegistrationDetailsException;
import com.Avy.bank.exceptions.UserExistException;

public interface UserService {
    UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException, InvalidRegistrationDetailsException;


    UserLoginResponse login(UserLoginRequest request) throws InvalidLoginCredentails;

    UserLogoutResponse logout(UserLogoutRequest request);
}
