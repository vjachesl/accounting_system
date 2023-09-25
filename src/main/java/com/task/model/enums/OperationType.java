package com.task.model.enums;

import com.task.exception.ValidationException;

import static com.task.exception.ErrorCodes.OPERATION_NOT_SUPPORTED;

public enum OperationType {
    DEBIT, CREDIT;

    public static OperationType getOperationType(String operation) {
        OperationType operationType;
        try {
            operationType = OperationType.valueOf(operation);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Operation '%s' is not valid", operation), OPERATION_NOT_SUPPORTED);
        }

        return operationType;
    }
}
