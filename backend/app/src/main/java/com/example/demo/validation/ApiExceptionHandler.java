package com.example.demo.validation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // helper method
    private ResponseEntity<Object> buildResponseEntity(ApiError a) {
        return new ResponseEntity<>(a, a.getStatus());
    }

    // Handles exception thrown when an entity is requested with an invalid ID
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return buildResponseEntity(error);
    }

    // Handles exception thrown when an entity is created or updated with invalid details
    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<Object> handleInvalidInput(InvalidInputException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        return buildResponseEntity(error);
    }


}
