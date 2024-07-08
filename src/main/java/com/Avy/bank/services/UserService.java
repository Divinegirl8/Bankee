package com.Avy.bank.services;

import com.Avy.bank.dtos.requests.OpenAccountRequest;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.OpenAccountResponse;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.InvalidRegistrationDetailsException;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.exceptions.UserNotFoundException;

public interface UserService {
    UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException, InvalidRegistrationDetailsException;


    OpenAccountResponse addAccount(OpenAccountRequest request) throws UserNotFoundException;
}
