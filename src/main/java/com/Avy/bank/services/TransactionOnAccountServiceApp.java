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


//    @Override
//    public ViewDepositResponse viewDeposit(ViewDepositRequest request) throws AccountNumberNotFound {
//        UserAccount existingAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
//        if (existingAccount == null) throw new AccountNumberNotFound("Invalid account number");
//
//        List<TransactionOnAccount> transactions = existingAccount.getTransactionOnAccountHistory();
//        ArrayList<TransactionOnAccount> existingTransactions = new ArrayList<>();
//        for (TransactionOnAccount transaction : transactions){
//            if (transaction.getTransactionType()== TransactionType.DEPOSIT)
//                existingTransactions.add(transaction);
//        }
//        if (existingTransactions.isEmpty()) throw new AccountNumberNotFound("There are not deposit transactions on this account");
//        ViewDepositResponse response = new ViewDepositResponse();
//        response.setDeposits(existingTransactions);
//        response.setMessage("Dear " + existingAccount.getAccountName() + "here's a list of  deposit transactions on your account: "
//                + response.getDeposits());
//        return response;
//    }

//    @Override
//    public void save(TransactionOnAccount transaction) {
//        transactionRepository.save(transaction);
//    }
}


