package com.Avy.bank.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDepositResponse {
    private Long transactionId;
    private String message;
}
