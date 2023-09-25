package com.task.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {
    
    @ExceptionHandler(NoElementFoundException.class)
    public ResponseEntity<AppError> handleItemNotFoundException(NoElementFoundException e ){
        log.error("Failed to find the requested element", e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getErrorCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AppError> handleValidationException(ValidationException e ){
        log.error("Validation exception has been thrown: {}", e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), e.getErrorCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(Exception e) {
        log.error("exception has been thrown", e);
        return new ResponseEntity<>(ErrorResponse.create(e, HttpStatus.SERVICE_UNAVAILABLE, e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
