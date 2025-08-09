package com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
