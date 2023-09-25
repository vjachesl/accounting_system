package com.task.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OperationDto {

    private long id;
    
    private long documentId;
        
    private String operationType;

    private String account;

    private String correspAccount;

    private BigDecimal creditAmount;
    
    private BigDecimal debitAmount;

    private String currency;

    private Date createdAt;
}
