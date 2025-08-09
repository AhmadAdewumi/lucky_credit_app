package com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message){
        super(message);
    }
}
