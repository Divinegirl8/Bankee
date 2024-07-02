package com.Avy.bank.dtos.responses;

import com.Avy.bank.data.models.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserFundTransferResponse {
    private TransactionStatus status;
    private String message;
//    private UUID transactionId;

}
