package com.task.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDocumentDto {
    private long id;

    private String operationType;

    private String account;

    private String correspAccount;

    private BigDecimal amount;

    private String currency;
    
    private String documentStatus;
}
