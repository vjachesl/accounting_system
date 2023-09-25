package com.task;

import com.task.domain.PaymentDocumentDto;
import com.task.model.Account;
import com.task.model.Company;
import com.task.model.Currency;
import com.task.model.PaymentDocument;
import com.task.model.enums.DocumentStatus;
import com.task.model.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

public class TestObjectHelper {
    public static final String UAH = "UAH";
    public static final String USD = "USD";
    public static final String OPERATION_CREDIT = "CREDIT";
    public static final String ACCOUNT_NORMAL = "UA345334634567450464576";
    public static final String ACCOUNT_CORRESP = "UA678789345290876787967";
    public static final Integer COMPANY_CODE = 12345678;

    public static PaymentDocument getNormalPaymentDocument() {
        return PaymentDocument.builder()
                .id(1)
                .operationType(OperationType.CREDIT)
                .account(getNormalUAHAccount())
                .correspAccount(getCorrespUAHAccount())
                .amount(BigDecimal.ONE)
                .currency(getUahCurrency())
                .documentStatus(DocumentStatus.NEW)
                .createdAt(new Date())
                .build();
    }
    public static PaymentDocumentDto getNormalPaymentDocumentDto(){
        PaymentDocumentDto paymentDocumentDto = new PaymentDocumentDto();
        paymentDocumentDto.setDocumentStatus("NEW");
        paymentDocumentDto.setAccount(ACCOUNT_NORMAL);
        paymentDocumentDto.setCorrespAccount(ACCOUNT_CORRESP);
        paymentDocumentDto.setAmount(BigDecimal.TEN);
        paymentDocumentDto.setCurrency(UAH);
        paymentDocumentDto.setOperationType(OPERATION_CREDIT);
        return paymentDocumentDto;
    }
    public static  Currency getUahCurrency(){
        Currency currency = new Currency();
        currency.setId(1);
        currency.setShortName(UAH);
        currency.setLongName("Ukrainian Hryvna");
        return currency;
    }

    public static Currency getUsdCurrency(){
        Currency currency = new Currency();
        currency.setId(2);
        currency.setShortName(USD);
        currency.setLongName("United States Dollar");
        return currency;
    }

    public static  Company getNormalCompany(){
        return Company.builder()
                .id(1)
                .companyName("Company name")
                .companyCode(COMPANY_CODE)
                .email("email@com")
                .phone("+23548905809")
                .build();
    }
    public static Account getNormalUAHAccount(){
        return Account.builder()
                .id(1)
                .accountName("Account name")
                .accountNumber(ACCOUNT_NORMAL)
                .currency(getUahCurrency())
                .company(getNormalCompany())
                .amount(BigDecimal.TEN)
                .build();
    }

    public static Account getCorrespUAHAccount(){
        return Account.builder()
                .id(1)
                .accountName("Account name")
                .accountNumber(ACCOUNT_CORRESP)
                .currency(getUahCurrency())
                .company(getNormalCompany())
                .amount(BigDecimal.TEN)
                .build();
    }
    
}
