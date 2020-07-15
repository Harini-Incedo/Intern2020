package com.example.demo.validation;

public class InvalidInputException extends RuntimeException {

    private String debugMessage;

    public InvalidInputException(String errorMessage, String debugMessage) {
        super(errorMessage);
        this.debugMessage = debugMessage;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

}
