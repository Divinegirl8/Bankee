package com.Avy.bank.services;

import com.Avy.bank.data.models.*;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.dtos.requests.*;
import com.Avy.bank.dtos.responses.*;
import com.Avy.bank.exceptions.AccountNumberNotFound;
import com.Avy.bank.exceptions.InvalidAmountException;
import com.Avy.bank.exceptions.TransactionException;
import com.Avy.bank.exceptions.UserNotFoundException;
import com.Avy.bank.utils.Validation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceApp  implements AccountService {


    private final AccountRepository accountRepository;
    private final TransactionOnAccountService transactionService;

    @Override
    public UserDepositResponse makeDeposit(UserDepositRequest request) throws InvalidAmountException, DescriptionException, TransactionException {
        UserAccount existingUserAccount = retrieveAccount(request.getAccountNumber(), request.getAccountName());
        validateAccess(existingUserAccount);
        validateTransactionRequest(request);
        TransactionOnAccount transaction = createTransaction(request, existingUserAccount);
        updateAccountBalanceAndTransactionHistory(existingUserAccount, transaction);
        accountRepository.save(existingUserAccount);
        return createResponse(transaction, existingUserAccount);
    }

    private static void validateAccess(UserAccount existingUserAccount) throws TransactionException {
        if (!existingUserAccount.isLogin()) throw new TransactionException("Login to process");
    }

    private UserAccount retrieveAccount(String accountNumber, String accountName){
        return accountRepository.findByAccountNumberAndAccountName(accountNumber, accountName);
    }

    private void validateTransactionRequest(UserDepositRequest request) throws InvalidAmountException, DescriptionException {
        validateDescription(request);
        validateDepositAmount(request.getAmount());
    }

    private void validateDescription(UserDepositRequest request) throws DescriptionException {
        if (Validation.validateDescription(request.getDescription())) throw new DescriptionException("Transaction description cannot be less than 1 character nor greater than 500 character");
    }

    private void validateDepositAmount(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Amount must be greater than zero");
    }

    private TransactionOnAccount createTransaction(UserDepositRequest request, UserAccount existingUserAccount) {
        TransactionOnAccount transaction = new TransactionOnAccount();
        transaction.setAccountNumber(existingUserAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccountName(request.getAccountName());
        transaction.setPerformedBy(request.getPerformedBy());
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDescription(request.getDescription());
        transaction.setPerformedAt(LocalDateTime.now());
        return transaction;
    }

    private void updateAccountBalanceAndTransactionHistory(UserAccount existingUserAccount, TransactionOnAccount transaction) {
        existingUserAccount.setBalance(existingUserAccount.getBalance().add(transaction.getAmount()));
        existingUserAccount.getTransactionOnAccountHistory().add(transaction);
    }

    private UserDepositResponse createResponse(TransactionOnAccount transaction, UserAccount existingUserAccount) {
        UserDepositResponse response = new UserDepositResponse();
        response.setTransactionId(transaction.getId());
        response.setMessage("Dear " + transaction.getPerformedBy() + " you have deposited N" + transaction.getAmount() + " into "
                + existingUserAccount.getAccountNumber() + ". Your transaction Id is: " + transaction.getId() +
                ". Thanks you for banking with us!");
        return response;
    }





@Override
    public UserBalanceResponse checkBalance(UserBalanceRequest request) throws AccountNumberNotFound, TransactionException {
        UserAccount existingAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (existingAccount==null) throw new AccountNumberNotFound("Invalid account number");
        validateAccess(existingAccount);
        BigDecimal balance = existingAccount.getBalance();
        UserBalanceResponse response = new UserBalanceResponse();
        response.setBalance(balance);
        response.setMessage("Dear " + existingAccount.getAccountName() +  " ,Your balance is: " + balance);
        return response;
    }

    @Override
    public UserWithdrawResponse makeWithdrawal(UserWithdrawRequest request) throws InvalidAmountException, AccountNumberNotFound, TransactionException {
        UserAccount existingUserAccount = getAccount(request.getAccountNumber(), request.getAccountName());
        validateAmount(request);
        validateAccess(existingUserAccount);
        validateWithdrawalAmount(request.getAmount(), existingUserAccount.getBalance());
        existingUserAccount.setBalance(subtractBalance(existingUserAccount, request.getAmount()));
        TransactionOnAccount transaction = createTransaction(existingUserAccount, request);
        transactionService.createTransaction(transaction);
        updateAccountTransactionHistory(existingUserAccount, transaction);
        return createResponse(existingUserAccount);
    }

    private static void validateAmount(UserWithdrawRequest request) throws InvalidAmountException {
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Amount must be greater than zero");
    }

    private UserAccount getAccount(String accountNumber, String accountName) throws AccountNumberNotFound {

        UserAccount account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.getAccountName().equals(accountName)) throw new AccountNumberNotFound("Invalid account details");
        return account;
    }

    private void validateWithdrawalAmount(BigDecimal amount, BigDecimal balance) throws InvalidAmountException {
        if (amount.subtract(balance).equals(BigDecimal.ZERO)) throw new InvalidAmountException("Insufficient balance");
    }

    private BigDecimal subtractBalance(UserAccount account, BigDecimal amount) {
        return account.getBalance().subtract(amount);
    }

    private TransactionOnAccount createTransaction(UserAccount account, UserWithdrawRequest request) {
        TransactionOnAccount transaction = new TransactionOnAccount();
        transaction.setAccountNumber(account.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccountName(request.getAccountName());
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDescription(request.getDescription());
        transaction.setPerformedBy(request.getPerformedBy());
        transaction.setAccount(account);
        transaction.setPerformedAt(LocalDateTime.now());
        return transaction;
    }

    private void updateAccountTransactionHistory(UserAccount account, TransactionOnAccount transaction) {
        account.getTransactionOnAccountHistory().add(transaction);
        accountRepository.save(account);
    }

    private UserWithdrawResponse createResponse(UserAccount account) {
        UserWithdrawResponse response = new UserWithdrawResponse();
        UserWithdrawRequest request = new UserWithdrawRequest();
        response.setBalance(account.getBalance());
        response.setMessage("Dear " + account.getAccountName() + ". You have successfully withdrawn " +
                request.getAmount() + ". Your acct balance: " + account.getBalance() + ". Thanks for banking with us!");
        return response;
    }


    @Override
    public UserFundTransferResponse transferFund(UserFundTransferRequest request) throws AccountNumberNotFound, InvalidAmountException, TransactionException {
    UserAccount existingAccountFrom = getAccount(request.getFromAccount());
        validateTransferAmount(request);
        validateAccess(existingAccountFrom);
    UserAccount existingAccountTo = getAccount(request.getToAccount());
    validateTransferAmount(existingAccountFrom, request.getAmount());
    updateAccountBalances(existingAccountFrom, existingAccountTo, request.getAmount());
    TransactionOnAccount transactionFrom = createTransaction(existingAccountFrom, request, TransactionType.TRANSFER);
    TransactionOnAccount transactionTo = createTransaction(existingAccountTo, request, TransactionType.CREDITED);

    transactionService.createTransaction(transactionFrom);
    transactionService.createTransaction(transactionTo);

    return createResponse(existingAccountFrom, existingAccountTo, request.getAmount());
    }

    private static void validateTransferAmount(UserFundTransferRequest request) throws InvalidAmountException {
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 1) throw new InvalidAmountException("Amount must be greater than zero");
    }

    @Override
    public ViewTransactionOnAccountResponse viewAllTransactions(ViewTransactionHistory request) throws AccountNumberNotFound, TransactionException {
        UserAccount existingAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (existingAccount == null) throw new AccountNumberNotFound("Invalid account number");
        validateAccess(existingAccount);

        List<TransactionOnAccount> transactions = existingAccount.getTransactionOnAccountHistory();
        List<TransactionOnAccount> existingTransactions = new ArrayList<>(transactions);
        ViewTransactionOnAccountResponse response = new ViewTransactionOnAccountResponse();
        response.setTransactionOnAccount(existingTransactions);
        response.setMessage("Dear " + existingAccount.getAccountName() + "here's a list of  deposit transactions on your account: "
                + response.getTransactionOnAccount());
        return response;
    }

    @Override
    public void saveUserAccount(UserAccount userAccount) {
        accountRepository.save(userAccount);
    }

    @Override
    public AccountLoginResponse login(AccountLoginRequest request) throws UserNotFoundException {
        UserAccount existingAccount = accountRepository.findByAccountEmail(request.getEmail());
        validateLoginCredentials(request, existingAccount);
        existingAccount.setLogin(true);
        accountRepository.save(existingAccount);
        return getLoginResponse();
    }

    @Override
    public AccountLogoutResponse logout(AccountLogoutRequest request) throws UserNotFoundException {
        UserAccount existingAccount = accountRepository.findById(request.getUserId()).orElse(null);
        if (existingAccount == null) throw new UserNotFoundException("Account not found");
        existingAccount.setLogin(false);
        accountRepository.save(existingAccount);
        AccountLogoutResponse response = new AccountLogoutResponse();
        response.setMessage("You have successfully logged out");
        return response;
    }

    private static AccountLoginResponse getLoginResponse() {
        AccountLoginResponse response = new AccountLoginResponse();
        response.setMessage("You have successfully logged in");
        return response;
    }

    private static void validateLoginCredentials(AccountLoginRequest request, UserAccount existingAccount) throws UserNotFoundException {
        if (existingAccount == null) throw new UserNotFoundException("Invalid login details");
        if (!existingAccount.getAccountPassword().equals(request.getPassword())) throw new UserNotFoundException("Invalid login details");
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

}
