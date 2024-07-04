package com.Avy.bank.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {
    private String email;
    private String password;
}
