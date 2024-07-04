package com.Avy.bank.controllers.userController;


import com.Avy.bank.dtos.requests.UserRegistrationRequest;
import com.Avy.bank.dtos.responses.UserRegistrationResponse;
import com.Avy.bank.exceptions.InvalidRegistrationDetailsException;
import com.Avy.bank.exceptions.UserExistException;
import com.Avy.bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request) throws UserExistException, InvalidRegistrationDetailsException {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }
}
