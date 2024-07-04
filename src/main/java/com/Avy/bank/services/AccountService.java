package com.Avy.bank.services;


import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.dtos.requests.*;
import com.Avy.bank.dtos.responses.*;
import com.Avy.bank.exceptions.*;

public interface AccountService {


    UserDepositResponse makeDeposit(UserDepositRequest request) throws AccountNumberNotFound, InvalidAmountException, DescriptionException, TransactionException;

    UserBalanceResponse checkBalance(UserBalanceRequest request) throws AccountNumberNotFound, TransactionException;

    UserWithdrawResponse makeWithdrawal(UserWithdrawRequest request) throws InvalidAmountException, AccountNumberNotFound, TransactionException;

    UserFundTransferResponse transferFund(UserFundTransferRequest request) throws AccountNumberNotFound, InvalidAmountException, CustomException, TransactionException;


    ViewTransactionOnAccountResponse viewAllTransactions(ViewTransactionHistory request) throws AccountNumberNotFound, TransactionException;

//    void createAccount(UserAccount userAccount);

    void saveUserAccount(UserAccount userAccount);

    AccountLoginResponse login(AccountLoginRequest request) throws UserNotFoundException;

    AccountLogoutResponse logout(AccountLogoutRequest request) throws UserNotFoundException;
}
