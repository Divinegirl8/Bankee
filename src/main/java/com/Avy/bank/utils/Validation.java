package com.Avy.bank.utils;

import com.Avy.bank.dtos.requests.DescriptionException;
import com.Avy.bank.exceptions.UserExistException;

public class Validation {

    public static boolean verifyPassword(String password){
        return password.matches("[A-Z][a-zA-Z]{2,}[0-9@!#$%^&():;.*_~`+{}]{1,9}");
    }

    public static boolean verifyEmail(String email){
        return email.matches("[a-zA-Z0-9!#$%^&():;.*_~`+{}]+@[a-z]+[.][a-z]{2,3}");

    }

    public static boolean validateDescription(String description) throws DescriptionException {
        if (description.isEmpty() || description.length() > 500) throw new DescriptionException("Transaction description cannot be less than 1 character nor greater than 500 character");
        return false;
    }

    public static void validateName(String name) throws UserExistException {
        if (name.isEmpty() || name.length() > 100) throw new UserExistException("Name cannot be less than 100 characters");
    }
}
