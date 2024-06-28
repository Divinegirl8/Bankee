package com.Avy.bank.services;

import com.Avy.bank.data.models.Account;
import com.Avy.bank.data.models.User;
import com.Avy.bank.data.repositories.AccountRepository;
import com.Avy.bank.data.repositories.UserRepository;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.utils.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        Account account = new Account();
        account.setAccountName(request.getFullName());
        account.setAccountType(request.getAccountType());
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setBalance(Long.valueOf(0));
        account.setCreatedAt(LocalDateTime.now());
        accountRepository.save(account);

        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setAddress(request.getAddress());
        newUser.setAccountType(request.getAccountType());
        newUser.setAccount(account);
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.save(newUser);



        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(newUser.getId());
        response.setMessage("Dear " + newUser.getFullName() + " your account number is " + account + " . Thanks for banking with us! We're thrilled to have you...");
        return response;
    }
}
