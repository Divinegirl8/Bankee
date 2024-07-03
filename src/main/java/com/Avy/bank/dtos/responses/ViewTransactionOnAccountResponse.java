package com.Avy.bank.dtos.responses;

import com.Avy.bank.data.models.TransactionOnAccount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ViewTransactionOnAccountResponse {
    private String message;
    private List<TransactionOnAccount> transactionOnAccount;
}
