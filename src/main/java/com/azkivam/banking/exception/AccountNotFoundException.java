package com.azkivam.banking.exception;

public class AccountNotFoundException extends GenericException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
