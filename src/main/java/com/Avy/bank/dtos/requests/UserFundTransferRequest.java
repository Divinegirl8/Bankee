package com.Avy.bank.dtos.requests;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UserFundTransferRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
    private String performedBy;

}
