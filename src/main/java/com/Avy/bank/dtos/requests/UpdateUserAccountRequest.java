package com.Avy.bank.dtos.requests;


import com.Avy.bank.data.models.AccountType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserAccountRequest {
    private String existingEmail;
    private String newEmail;
    private String newPassword;
    private String password;
    private String existingFullName;
    private String newFullName;
    private String existingAddress;
    private String newAddress;
    private String existingPhoneNumber;
    private String newPhoneNumber;
    private AccountType existingAccountType;
    private AccountType newAccountType;
}
