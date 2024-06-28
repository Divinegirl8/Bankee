package com.Avy.bank.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserDepositRequest {
    private String accountNumber;
    private Double amount;
    private String description;
    private String depositor;
    private LocalDateTime depositDate;

}
