package com.Avy.bank.dtos.requests;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UserWithdrawRequest {
    private String accountNumber;
    private String accountName;
    private BigDecimal amount;
    private String description;
    private String performedBy;
    private String performedAt;

}
