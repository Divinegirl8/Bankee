package com.Avy.bank.services;


import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.exceptions.AccountNumberNotFound;

public interface AccountService {
    UserDepositResponse deposit(UserDepositRequest request) throws AccountNumberNotFound;
}
