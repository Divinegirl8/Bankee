package com.Avy.bank.services;

import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.User;
import com.Avy.bank.data.repositories.UserRepository;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.InvalidRegistrationDetailsException;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.utils.AccountNumberGenerator;
import com.Avy.bank.utils.Validation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    private final UserRepository userRepository;
    private final AccountService accountService;


    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException, InvalidRegistrationDetailsException {
        if (isUserAlreadyRegistered(request.getEmail())) throw new UserExistException("User already exist");
        if (!areRegistrationDetailsValid(request)) throw new InvalidRegistrationDetailsException("");
        User newUser = createUser(request);
        UserAccount userAccount = createUserAccount(request, newUser);
        saveUser(newUser);
        accountService.saveUserAccount(userAccount);
        return createUserRegistrationResponse(newUser, userAccount);
    }

    private boolean isUserAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean areRegistrationDetailsValid(UserRegistrationRequest request) {
        return Validation.verifyEmail(request.getEmail()) && Validation.verifyPassword(request.getPassword());
    }

    private User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    private UserAccount createUserAccount(UserRegistrationRequest request, User user) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setAccountName(request.getFullName());
        userAccount.setAccountEmail(user.getEmail());
        userAccount.setAccountPassword(user.getPassword());
        userAccount.setAccountType(request.getAccountType());
        userAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
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