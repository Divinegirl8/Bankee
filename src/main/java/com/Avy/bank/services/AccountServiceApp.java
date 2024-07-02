package com.Avy.bank.services;

import com.Avy.bank.data.models.TransactionStatus;
import com.Avy.bank.data.models.TransactionType;
import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.dtos.requests.UserBalanceRequest;
import com.Avy.bank.dtos.requests.UserDepositRequest;
import com.Avy.bank.dtos.requests.UserFundTransferRequest;
import com.Avy.bank.dtos.requests.UserWithdrawRequest;
import com.Avy.bank.dtos.responses.UserBalanceResponse;
import com.Avy.bank.dtos.responses.UserDepositResponse;
import com.Avy.bank.dtos.responses.UserFundTransferResponse;
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
        existingUserAccount.setTransactionOnAccountHistory(listOfTransactionOnAccounts);
        accountRepository.save(existingUserAccount);

        UserDepositResponse response = new UserDepositResponse();
        response.setTransactionId(transaction.getId());
        response.setMessage("Dear " + transaction.getPerformedBy() + " you have deposited N" + request.getAmount() +
                " into " + existingUserAccount.getAccountNumber() +
                ". Your transaction Id is: " + transaction.getId() +
                ". Thanks you for banking with us!");
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
    public UserWithdrawResponse makeWithdrawal(UserWithdrawRequest request) throws InvalidAmountException, AccountNumberNotFound {
        UserAccount existingUserAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (request.getAmount().subtract(existingUserAccount.getBalance()).equals(BigDecimal.ZERO))
            throw new InvalidAmountException("Insufficient balance");
        if (!request.getAccountName().equals(existingUserAccount.getAccountName()))
            throw new AccountNumberNotFound("Invalid account details");

        List<TransactionOnAccount>  transactionHistory =  existingUserAccount.getTransactionOnAccountHistory();

        existingUserAccount.setBalance(existingUserAccount.getBalance().subtract(request.getAmount()));

        TransactionOnAccount transaction = new TransactionOnAccount();
        transaction.setAccountNumber(existingUserAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccountName(request.getAccountName());
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDescription(request.getDescription());
        transaction.setPerformedBy(request.getPerformedBy());
        transaction.setAccount(existingUserAccount);
        transaction.setPerformedAt(LocalDateTime.now());
        transactionHistory.add(transaction);

        transactionOnAccountService.save(transaction);

        existingUserAccount.setTransactionOnAccountHistory(transactionHistory);
        accountRepository.save(existingUserAccount);


        UserWithdrawResponse response = new UserWithdrawResponse();
        response.setBalance(existingUserAccount.getBalance());
        response.setMessage("Dear " + existingUserAccount.getAccountName() + ". You have successfully withdrawn " + request.getAmount() + ". Your acct balance: " + existingUserAccount.getBalance() + ". Thanks for banking with us!");
        return response;
    }

    @Override
    public UserFundTransferResponse transferFund(UserFundTransferRequest request) throws AccountNumberNotFound, InvalidAmountException {

        UserAccount existingAccountFrom = accountRepository.findByAccountNumber(request.getFromAccount());
        if (existingAccountFrom == null) throw new AccountNumberNotFound("Invalid account number");

        UserAccount existingAccountTo = accountRepository.findByAccountNumber(request.getToAccount());
        if (existingAccountTo == null) throw new AccountNumberNotFound("Invalid account number");

        if (existingAccountFrom.getBalance().subtract(request.getAmount()).compareTo(BigDecimal.ZERO) < 1)
            throw new InvalidAmountException("Insufficient fund");

        List<TransactionOnAccount> existingTransactionOnAccountFrom = existingAccountFrom.getTransactionOnAccountHistory();

        List<TransactionOnAccount> existingTransactionOnAccountsTo = existingAccountTo.getTransactionOnAccountHistory();

        existingAccountFrom.setBalance(existingAccountFrom.getBalance().subtract(request.getAmount()));

        existingAccountTo.setBalance(existingAccountTo.getBalance().add(request.getAmount()));


        TransactionOnAccount transactionFrom = new TransactionOnAccount();
        transactionFrom.setAccountNumber(existingAccountFrom.getAccountNumber());
        transactionFrom.setAccountName(existingAccountFrom.getAccountName());
        transactionFrom.setAmount(request.getAmount());
        transactionFrom.setTransactionType(TransactionType.TRANSFER);
        transactionFrom.setStatus(TransactionStatus.SUCCESSFUL);
        transactionFrom.setPerformedBy(existingAccountFrom.getAccountName());
        transactionFrom.setAccount(existingAccountFrom);
        transactionFrom.setDescription(request.getDescription());
        transactionFrom.setPerformedAt(LocalDateTime.now());
        existingTransactionOnAccountFrom.add(transactionFrom);
        transactionOnAccountService.save(transactionFrom);

        existingAccountFrom.setTransactionOnAccountHistory(existingTransactionOnAccountFrom);


        TransactionOnAccount transactionTo = new TransactionOnAccount();
        transactionTo.setAccountNumber(existingAccountTo.getAccountNumber());
        transactionTo.setAccountName(existingAccountTo.getAccountName());
        transactionTo.setAmount(request.getAmount());
        transactionTo.setTransactionType(TransactionType.TRANSFER);
        transactionTo.setStatus(TransactionStatus.SUCCESSFUL);
        transactionTo.setDescription(request.getDescription());
        transactionTo.setPerformedBy(existingAccountTo.getAccountName());
        transactionTo.setAccount(existingAccountTo);
        transactionTo.setPerformedAt(LocalDateTime.now());
        existingTransactionOnAccountsTo.add(transactionTo);
        existingTransactionOnAccountsTo.add(transactionTo);

        transactionOnAccountService.save(transactionFrom);

        existingAccountTo.setTransactionOnAccountHistory(existingTransactionOnAccountsTo);

        accountRepository.save(existingAccountFrom);
        accountRepository.save(existingAccountTo);

        UserFundTransferResponse response = new UserFundTransferResponse();
        response.setStatus(TransactionStatus.SUCCESSFUL);
        response.setMessage("Dear " + existingAccountFrom.getAccountName() +
                " ,you have successfully transferred "
                + request.getAmount() + " to "
                + existingAccountTo.getAccountNumber());
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
