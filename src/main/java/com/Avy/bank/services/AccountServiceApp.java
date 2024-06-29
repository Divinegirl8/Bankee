package com.Avy.bank.services;

import com.Avy.bank.data.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceApp  implements AccountService {


    private final AccountRepository accountRepository;

    @Override
    public String findAccountNumber(String accountNumber) {
        return accountRepository.findAccountNumber(accountNumber);
    }
}
