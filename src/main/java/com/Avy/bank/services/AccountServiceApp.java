package com.Avy.bank.services;

import com.Avy.bank.data.models.TransactionStatus;
import com.Avy.bank.data.models.TransactionType;
import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.dtos.requests.UserBalanceRequest;
import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.requests.UserWithdrawRequest;
import com.Avy.bank.dtos.responses.UserBalanceResponse;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.dtos.responses.UserWithdrawResponse;
import com.Avy.bank.exceptions.AccountNumberNotFound;
import com.Avy.bank.exceptions.InvalidAmountException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceApp  implements AccountService {


    private final AccountRepository accountRepository;
    private final TransactionOnAccountService transactionOnAccountService;



    @Override
    public UserDepositResponse makeDeposit(UserDepositRequest request) throws AccountNumberNotFound, InvalidAmountException {
        UserAccount existingUserAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (existingUserAccount == null) throw new AccountNumberNotFound("Account not found");

        if (!existingUserAccount.getAccountName().equals(request.getAccountName())) throw new AccountNumberNotFound("Account name not matched");

        List<TransactionOnAccount> listOfTransactionOnAccounts = existingUserAccount.getTransactionOnAccountHistory();
        BigDecimal transactionAmount = new BigDecimal(String.valueOf(request.getAmount()));

        if (transactionAmount.compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Amount must be greater than zero");

        TransactionOnAccount transaction = getTransactionOnAccount(request, existingUserAccount);
        existingUserAccount.setBalance(existingUserAccount.getBalance().add(transaction.getAmount()));

        listOfTransactionOnAccounts.add(transaction);
        existingUserAccount.setTransactionOnAccountHistory(listOfTransactionOnAccounts);

        transactionOnAccountService.save(transaction);
        accountRepository.save(existingUserAccount);

        UserDepositResponse response = new UserDepositResponse();
        response.setTransactionId(transaction.getId());
        response.setMessage("Dear " + transaction.getPerformedBy() + " you have deposited N" + request.getAmount() + " into " + existingUserAccount.getAccountNumber() + ". Your transaction Id is: " + transaction.getId() +  ". Thanks you for banking with us!");
        return response;
    }

    @Override
    public UserBalanceResponse checkBalance(UserBalanceRequest request) throws AccountNumberNotFound {

        UserAccount existingAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (existingAccount==null) throw new AccountNumberNotFound("Invalid account number");
        BigDecimal balance = existingAccount.getBalance();
        UserBalanceResponse response = new UserBalanceResponse();
        response.setBalance(balance);
        response.setMessage("Dear " + existingAccount.getAccountName() +  " ,Your balance is: " + balance);
        return response;
    }

    @Override
    public UserWithdrawResponse makeWithdraw(UserWithdrawRequest request) throws InvalidAmountException, AccountNumberNotFound {
        UserAccount existingUserAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (request.getAmount().subtract(existingUserAccount.getBalance()).equals(BigDecimal.ZERO))
            throw new InvalidAmountException("Insufficient balance");

        List<TransactionOnAccount>  transactionHistory =  existingUserAccount.getTransactionOnAccountHistory();

        existingUserAccount.setBalance(existingUserAccount.getBalance().subtract(request.getAmount()));

        TransactionOnAccount transaction = new TransactionOnAccount();
        transaction.setAccountNumber(existingUserAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccountName(request.getAccountName());
        if (!request.getAccountName().equals(existingUserAccount.getAccountName())) throw new AccountNumberNotFound("Invalid account details");
        transaction.setDescription(request.getDescription());
        transaction.setPerformedBy(request.getPerformedBy());
        transaction.setAccount(existingUserAccount);
        transaction.setPerformedAt(LocalDateTime.now());
        transactionHistory.add(transaction);

        transactionOnAccountService.save(transaction);

        accountRepository.save(existingUserAccount);


        UserWithdrawResponse response = new UserWithdrawResponse();
        response.setBalance(existingUserAccount.getBalance());
        response.setMessage("Dear " + existingUserAccount.getAccountName() + ". You have successfully withdrawn " + request.getAmount() + ". Your acct balance: " + existingUserAccount.getBalance() + ". Thanks for banking with us!");
        return response;
    }

    private static TransactionOnAccount getTransactionOnAccount(UserDepositRequest request, UserAccount existingUserAccount) {
        TransactionOnAccount transaction = new TransactionOnAccount();
        transaction.setAmount(request.getAmount());
        transaction.setAccountNumber(existingUserAccount.getAccountNumber());
        transaction.setAccountName(existingUserAccount.getAccountName());
        transaction.setPerformedBy(request.getDepositor());
        transaction.setDescription(request.getDescription());
        transaction.setAccount(existingUserAccount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setPerformedAt(LocalDateTime.now());
        return transaction;
    }
}
