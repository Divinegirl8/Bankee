package com.Avy.bank.exceptions;

public class UserExistException extends Throwable {
    public UserExistException(String message) {
        super(message);
    }
}
