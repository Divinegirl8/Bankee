package com.Avy.bank.services;


import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.models.TransactionStatus;
import com.Avy.bank.data.models.TransactionType;
import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.exceptions.InvalidAccountNumberException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class TransactionOnAccountServiceApp implements TransactionOnAccountService {

    private final AccountService accountService;

    @Override
    public UserDepositResponse makeDeposit(UserDepositRequest request) throws InvalidAccountNumberException {
        String existingAccountNumber = accountService.findAccountNumber(request.getAccountNumber());

        if (existingAccountNumber == null) throw new InvalidAccountNumberException(TransactionStatus.FAILED,"Invalid account number");

        TransactionOnAccount transaction = new TransactionOnAccount();



        transaction.setAmount(request.getAmount());
        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setPerformedAt(LocalDateTime.now());




        return null;
    }
}


