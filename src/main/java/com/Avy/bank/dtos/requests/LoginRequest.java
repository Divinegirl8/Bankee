package com.Avy.bank.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LoginRequest {
    private Long userId;
    private String password;
    private String email;
}
