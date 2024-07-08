package com.Avy.bank.services;

import com.Avy.bank.data.models.TransactionOnAccount;
import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.User;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.data.repositories.UserRepository;
import com.Avy.bank.dtos.requests.OpenAccountRequest;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.OpenAccountResponse;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.InvalidRegistrationDetailsException;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.exceptions.UserNotFoundException;
import com.Avy.bank.utils.AccountNumberGenerator;
import com.Avy.bank.utils.Validation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;


    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException, InvalidRegistrationDetailsException {
        if (isUserAlreadyRegistered(request.getEmail())) {
            throw new UserExistException("User already exist");
        }
        if (!areRegistrationDetailsValid(request)) {
            throw new InvalidRegistrationDetailsException("");
        }
        User newUser = createUser(request);
        UserAccount userAccount = createUserAccount(request, newUser);
        List<UserAccount> newUserListOfAccounts = newUser.getUserAccount();
        newUserListOfAccounts.add(userAccount);

        saveUser(newUser);
        accountService.saveUserAccount(userAccount);
        return createUserRegistrationResponse(newUser, userAccount);
    }

    @Override
    public OpenAccountResponse addAccount(OpenAccountRequest request) throws UserNotFoundException {
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser == null) throw new UserNotFoundException("Invalid user details");

        List<TransactionOnAccount> listOfTransactions = new ArrayList<>();
        UserAccount newAccount = new UserAccount();
        newAccount.setAccountName(existingUser.getFullName());
        newAccount.setAccountEmail(existingUser.getEmail());
        newAccount.setAccountType(request.getAccountType());
        newAccount.setAccountPassword(existingUser.getPassword());
        newAccount.setUserId(existingUser.getId());
        newAccount.setTransactionOnAccountHistory(listOfTransactions);
        String generatedAccountNumber;
        do {
            generatedAccountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.findByAccountNumber(generatedAccountNumber) != null);

        newAccount.setAccountNumber(generatedAccountNumber);
        newAccount.setBalance(BigDecimal.valueOf(0));
        newAccount.setCreatedAt(LocalDateTime.now());
        accountService.saveUserAccount(newAccount);

        List<UserAccount> existingUserAccounts = existingUser.getUserAccount();
        existingUserAccounts.add(newAccount);
        existingUser.setUserAccount(existingUserAccounts);
        userRepository.save(existingUser);
        OpenAccountResponse response = new OpenAccountResponse();
        response.setMessage("Dear " + existingUser.getFullName() +
                " you have successfully open an additional account with accountNumber "
                + newAccount.getAccountNumber() + " .Thanks for banking with us....");
        return response;
    }

    private boolean isUserAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean areRegistrationDetailsValid(UserRegistrationRequest request) {
        return Validation.verifyEmail(request.getEmail()) && Validation.verifyPassword(request.getPassword());
    }

    private User createUser(UserRegistrationRequest request) {
        User user = new User();

        List<UserAccount> userAccounts = new ArrayList<>();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setUserAccount(userAccounts);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return user;
    }

    private UserAccount createUserAccount(UserRegistrationRequest request, User user) {
        List<TransactionOnAccount> listOfTransactions = new ArrayList<>();
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setAccountName(request.getFullName());
        userAccount.setAccountEmail(user.getEmail());
        userAccount.setAccountPassword(user.getPassword());
        userAccount.setAccountType(request.getAccountType());
        userAccount.setTransactionOnAccountHistory(listOfTransactions);



        String generatedAccountNumber;
        do {
            generatedAccountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.findByAccountNumber(generatedAccountNumber) != null);

        userAccount.setAccountNumber(generatedAccountNumber);
        userAccount.setBalance(BigDecimal.valueOf(0));
        userAccount.setCreatedAt(LocalDateTime.now());

        return userAccount;
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private UserRegistrationResponse createUserRegistrationResponse(User user, UserAccount userAccount) {
        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(user.getId());
        response.setMessage("Dear " + user.getFullName() + " your account number is " + userAccount.getAccountNumber() + ". Thanks for banking with us! We're thrilled to have you...");
        return response;
    }


}