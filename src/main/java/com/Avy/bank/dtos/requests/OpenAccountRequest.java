package com.Avy.bank.dtos.requests;


import com.Avy.bank.data.models.AccountType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OpenAccountRequest {
    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private AccountType accountType;
}
