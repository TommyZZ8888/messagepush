package com.zz.messagepush.common.exception;

public class ErrorException extends RuntimeException {

    private final String message;

    public String getMessage(){
        return message;
    }

    public ErrorException(String message) {
        super(message);
        this.message = message;
    }
}
