package com.example.demo.validation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String errorMessage;
    private String debugMessage;

    ApiError(HttpStatus status, String errorMessage, Throwable ex) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.errorMessage = errorMessage;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

}
