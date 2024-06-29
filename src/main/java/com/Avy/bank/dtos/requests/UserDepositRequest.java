package com.Avy.bank.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDepositRequest {
    private String accountName;
    private String accountNumber;
    private Long amount;
    private String description;
    private String depositor;
    private String depositDate;

}
