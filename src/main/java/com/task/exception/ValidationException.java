package com.task.exception;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException {
    
    private final ErrorCodes errorCode;

    public ValidationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
