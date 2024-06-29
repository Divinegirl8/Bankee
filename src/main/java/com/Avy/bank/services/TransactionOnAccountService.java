package com.Avy.bank.services;

import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.exceptions.InvalidAccountNumberException;

public interface TransactionOnAccountService {


    UserDepositResponse makeDeposit(UserDepositRequest request) throws InvalidAccountNumberException;
}
