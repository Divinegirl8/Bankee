package com.Avy.bank.services;


import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class TransactionOnAccountServiceApp implements TransactionOnAccountService {

    private final TransactionRepository transactionRepository;


    @Override
    public void createTransaction(TransactionOnAccount transactionFrom) {
        transactionRepository.save(transactionFrom);

    }

}
