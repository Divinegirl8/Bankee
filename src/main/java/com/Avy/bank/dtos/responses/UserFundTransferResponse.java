package com.Avy.bank.dtos.responses;

import com.Avy.bank.data.models.TransactionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserFundTransferResponse {
    private TransactionStatus status;
    private String message;
//    private UUID transactionId;

}
