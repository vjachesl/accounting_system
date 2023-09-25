package com.task.exception;

import lombok.Data;

@Data
public class NoElementFoundException extends RuntimeException{
    private final ErrorCodes errorCode;

    public NoElementFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
