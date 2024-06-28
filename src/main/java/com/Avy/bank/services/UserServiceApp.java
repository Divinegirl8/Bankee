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
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException {
        User user = userRepository.findByEmail(request.getEmail());
        boolean isRegistered = user != null;
        if (isRegistered) throw new UserExistException("User already exist");


        User newUser = new User();

        ArrayList<Account> accounts = new ArrayList<>();
        Account acc = new Account();
        acc.setAccountName(request.getFullName());
        acc.setAccountType(request.getAccountType());
        acc.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        acc.setCreatedAt(LocalDateTime.now());

        String accountNumber = acc.getAccountNumber();

        String existingAccountNumber = accountRepository.findByAccountNumber(acc.getAccountNumber());

        if (existingAccountNumber != null && !existingAccountNumber.equals(accountNumber)) {
            throw new UserExistException("Account number already exist");
        }


        accounts.add(acc);

        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setAddress(request.getAddress());
        newUser.setAccountType(request.getAccountType());
        newUser.setAccount(accounts);
        newUser.setCreatedAt(LocalDateTime.now());
        accountRepository.save(acc);
        userRepository.save(newUser);



        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(newUser.getId());
        response.setMessage("Dear " + newUser.getFullName() + " your account number is " + accountNumber + " . Thanks for banking with us! We're thrilled to have you...");
        return response;
    }
}
