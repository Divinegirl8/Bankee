package com.Avy.bank.services;

import com.Avy.bank.data.models.TransactionStatus;
import com.Avy.bank.data.models.TransactionType;
import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.data.repositories.TransactionRepository;
import com.Avy.bank.dtos.requests.*;
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
    private final TransactionRepository transactionRepository;
    private final TransactionOnAccountService transactionService;

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

//        transactionOnAccountService.save(transaction);
        transactionRepository.save(transaction);

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

//        transactionOnAccountService.save(transaction);
        transactionRepository.save(transaction);

        existingUserAccount.setTransactionOnAccountHistory(transactionHistory);
        accountRepository.save(existingUserAccount);


        UserWithdrawResponse response = new UserWithdrawResponse();
        response.setBalance(existingUserAccount.getBalance());
        response.setMessage("Dear " + existingUserAccount.getAccountName() + ". You have successfully withdrawn " + request.getAmount() + ". Your acct balance: " + existingUserAccount.getBalance() + ". Thanks for banking with us!");
        return response;
    }

    @Override
    public UserFundTransferResponse transferFund(UserFundTransferRequest request) throws AccountNumberNotFound, InvalidAmountException {
    UserAccount existingAccountFrom = getAccount(request.getFromAccount());
    UserAccount existingAccountTo = getAccount(request.getToAccount());
    validateTransferAmount(existingAccountFrom, request.getAmount());
    updateAccountBalances(existingAccountFrom, existingAccountTo, request.getAmount());
    TransactionOnAccount transactionFrom = createTransaction(existingAccountFrom, request, TransactionType.TRANSFER);
    TransactionOnAccount transactionTo = createTransaction(existingAccountTo, request, TransactionType.CREDITED);

    transactionService.createTransaction(transactionFrom);
    transactionService.createTransaction(transactionTo);

    return createResponse(existingAccountFrom, existingAccountTo, request.getAmount());
    }


    private UserAccount getAccount(String accountNumber) throws AccountNumberNotFound {
    UserAccount account = accountRepository.findByAccountNumber(accountNumber);
    if (account == null) throw new AccountNumberNotFound("Invalid account number");
    return account;
    }

    private void validateTransferAmount(UserAccount account, BigDecimal amount) throws InvalidAmountException {
    if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Insufficient fund");
    }

    private void updateAccountBalances(UserAccount from, UserAccount to, BigDecimal amount) {
    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));
    accountRepository.save(from);
    accountRepository.save(to);
    }

    private TransactionOnAccount createTransaction(UserAccount account, UserFundTransferRequest request, TransactionType type) {
    TransactionOnAccount transaction = new TransactionOnAccount();
    transaction.setAccount(account);
    transaction.setAccountNumber(account.getAccountNumber());
    transaction.setAccountName(account.getAccountName());
    transaction.setAmount(request.getAmount());
    transaction.setPerformedBy(request.getPerformedBy());
    transaction.setTransactionType(type);
    transaction.setStatus(TransactionStatus.SUCCESSFUL);
    transaction.setDescription(request.getDescription());
    transaction.setPerformedAt(LocalDateTime.now());
    return transaction;
    }

    private UserFundTransferResponse createResponse(UserAccount from, UserAccount to, BigDecimal amount) {
    UserFundTransferResponse response = new UserFundTransferResponse();
    response.setStatus(TransactionStatus.SUCCESSFUL);
    response.setMessage("Dear " + from.getAccountName() + " ,you have successfully transferred " + amount + " to " + to.getAccountNumber());
    return response;
    }


//    public UserFundTransferResponse transferFund(UserFundTransferRequest request) throws AccountNumberNotFound, InvalidAmountException, CustomException {
//        UserAccount existingAccountFrom = accountRepository.findByAccountNumber(request.getFromAccount());
//        if (existingAccountFrom == null) throw new AccountNumberNotFound("Invalid account number");
//        UserAccount existingAccountTo = accountRepository.findByAccountNumber(request.getToAccount());
//        if (existingAccountTo == null) throw new AccountNumberNotFound("Invalid account number");
//        if (existingAccountFrom.getBalance().subtract(request.getAmount()).compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Insufficient fund");
//
//        existingAccountFrom.setBalance(existingAccountFrom.getBalance().subtract(request.getAmount()));
//        existingAccountTo.setBalance(existingAccountTo.getBalance().add(request.getAmount()));
//
//        accountRepository.save(existingAccountFrom);
//        accountRepository.save(existingAccountTo);
//
//        TransactionOnAccount transactionFrom = new TransactionOnAccount();
//        transactionFrom.setAccount(existingAccountFrom);
//        transactionFrom.setAccountNumber(existingAccountFrom.getAccountNumber());
//        transactionFrom.setAccountName(existingAccountFrom.getAccountName());
//        transactionFrom.setAmount(request.getAmount());
//        transactionFrom.setPerformedBy(request.getPerformedBy());
//        transactionFrom.setTransactionType(TransactionType.TRANSFER);
//        transactionFrom.setStatus(TransactionStatus.SUCCESSFUL);
//        transactionFrom.setDescription(request.getDescription());
//        transactionFrom.setPerformedAt(LocalDateTime.now());
//
//        TransactionOnAccount transactionTo = new TransactionOnAccount();
//        transactionTo.setAccount(existingAccountTo);
//        transactionTo.setAccountNumber(existingAccountTo.getAccountNumber());
//        transactionTo.setAccountName(existingAccountTo.getAccountName());
//        transactionTo.setAmount(request.getAmount());
//        transactionTo.setPerformedBy(request.getPerformedBy());
//        transactionTo.setTransactionType(TransactionType.CREDITED);
//        transactionTo.setStatus(TransactionStatus.SUCCESSFUL);
//        transactionTo.setDescription(request.getDescription());
//        transactionTo.setPerformedAt(LocalDateTime.now());
//
//        transactionService.createTransaction(transactionFrom);
//        transactionService.createTransaction(transactionTo);
//
//        UserFundTransferResponse response = new UserFundTransferResponse();
//        response.setStatus(TransactionStatus.SUCCESSFUL);
//        response.setMessage("Dear " + existingAccountFrom.getAccountName() + " ,you have successfully transferred " + request.getAmount() + " to " + existingAccountTo.getAccountNumber());
//        return response;
//    }

    @Override
    public UserAccount findByAccountNumber(String fromAccount) {
        return accountRepository.findByAccountNumber(fromAccount);
    }

//    @Override
//    public UserAccount findByAccountNumber(ViewDepositRequest request) {
//        return accountRepository.findByAccountNumber(request.getAccountNumber());
//    }

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
