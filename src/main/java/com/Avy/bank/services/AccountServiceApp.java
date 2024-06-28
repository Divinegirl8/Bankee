package com.Avy.bank.services;

import com.Avy.bank.data.models.Account;
import com.Avy.bank.data.models.Transaction;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.exceptions.AccountNumberNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AccountServiceApp  implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public UserDepositResponse deposit(UserDepositRequest request) throws AccountNumberNotFound {

        Account existingAccount = accountRepository.findByAccountNumber(request.getAccountNumber());

        ArrayList<Transaction> transactionHistory = new ArrayList<>();

        if (existingAccount == null) throw new AccountNumberNotFound("Invalid account number");
        existingAccount.setBalance(existingAccount.getBalance() + request.getAmount());


        existingAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(existingAccount);

        UserDepositResponse response = new UserDepositResponse();
        response.setMessage("Successfully deposited " + request.getAmount() + " from " + existingAccount.getAccountNumber());
        return response;
    }
}
