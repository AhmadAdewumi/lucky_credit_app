package com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions;

public class InsufficientFundException extends RuntimeException{
    public InsufficientFundException(String message) {
        super(message);
    }
}
