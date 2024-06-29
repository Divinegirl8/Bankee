package com.Avy.bank.exceptions;

import com.Avy.bank.data.models.TransactionStatus;

public class InvalidAccountNumberException extends Exception {
    public InvalidAccountNumberException(TransactionStatus transactionStatus,String message) {
        super(message);
    }
}
