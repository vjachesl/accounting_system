package com.task.exception;

public enum ErrorCodes {
    EMPTY_FIELD(1),
    EMPTY_COMPANY_NAME_FIELD(2),
    NOT_VALID_COMPANY_CODE(3),
    NON_VALID_EMAIL(4),
    NO_ELEMENT(5),
    DUPLICATE_COMPANY(6),
    DUPLICATE_ACCOUNT(7),
    OPERATION_NOT_SUPPORTED(8),
    AMOUNT_SHOULD_BE_GREATER_THEN_ZERO(9),
    INVALID_CURRENCY(10),
    CURRENCIES_MUST_BE_EQUAL(11),
    NOT_ENOUGH_MONEY_ON_ACCOUNT(12);
    private int code;
    ErrorCodes(int code) {
        this.code = code;
    }
}
