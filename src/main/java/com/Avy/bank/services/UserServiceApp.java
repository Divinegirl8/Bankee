package com.Avy.bank.services;

import com.Avy.bank.data.models.UserAccount;
import com.Avy.bank.data.models.User;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.data.repositories.UserRepository;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.utils.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException {
        boolean isRegistered  = userRepository.findByEmail(request.getEmail()) !=null;
        if (isRegistered) throw new UserExistException("User already exist");

        User newUser = new User();

        ArrayList<UserAccount> accounts = new ArrayList<>();

        UserAccount userAccount = new UserAccount();

        userAccount.setAccountName(request.getFullName());
        userAccount.setAccountType(request.getAccountType());
        userAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        userAccount.setBalance(BigDecimal.valueOf(0));
        userAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(userAccount);
        userRepository.save(newUser);

        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setAddress(request.getAddress());
        accounts.add(userAccount);
        newUser.setUserAccount(accounts);
        newUser.setCreatedAt(LocalDateTime.now());

        userAccount.setUserId(newUser.getId());

        userRepository.save(newUser);
        accountRepository.save(userAccount);



        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(newUser.getId());
        response.setMessage("Dear " + newUser.getFullName() + " your account number is " + userAccount + " . Thanks for banking with us! We're thrilled to have you...");
        return response;
    }

}