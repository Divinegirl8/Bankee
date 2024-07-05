package com.Avy.bank.exceptions;

import com.Avy.bank.data.models.TransactionStatus;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message, TransactionStatus status) {
        super(message);
    }
}
