package com.task.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String accountNumber;

    private String accountName;

    private String currency;

    private BigDecimal amount;

    private Integer companyCode;
}
