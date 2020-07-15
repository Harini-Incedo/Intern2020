package com.example.demo.validation;

public class EntityNotFoundException extends RuntimeException {

    private String debugMessage;

    public EntityNotFoundException(String errorMessage, String debugMessage) {
        super(errorMessage);
        this.debugMessage = debugMessage;
    }

    public String getDebugMessage() {
        return debugMessage;
    }


}
