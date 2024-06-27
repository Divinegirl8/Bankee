package com.Avy.bank.services;

import com.Avy.bank.data.models.Account;
import com.Avy.bank.data.models.User;
import com.Avy.bank.data.repositories.UserRepository;
import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.UserExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserServiceApp implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest request) throws UserExistException {
        User user = userRepository.findByEmail(request.getEmail());
        boolean isRegistered = user != null;
        if (isRegistered) throw new UserExistException("User already exist");



        User newUser = new User();

        ArrayList<Account> accounts = new ArrayList<>();

        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setAddress(request.getAddress());
        newUser.setAccountType(request.getAccountType());
        newUser.setAccount(accounts);
        newUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(newUser);

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(newUser.getId());
        response.setMessage("User registered successfully");
        return response;
    }
}
