package com.task.service;

import com.task.domain.PaymentDocumentDto;
import com.task.exception.NoElementFoundException;
import com.task.exception.ValidationException;
import com.task.model.*;
import com.task.model.enums.DocumentStatus;
import com.task.model.enums.OperationType;
import com.task.repository.PaymentDocumentRepository;
import com.task.util.NullChecker;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.task.exception.ErrorCodes.*;
import static com.task.exception.ErrorCodes.NO_ELEMENT;

@Slf4j
@Service
public class PaymentDocumentService {

    CurrencyService currencyService;
    AccountService accountService;
    PaymentDocumentRepository paymentDocumentRepository;
    ModelMapper mapper;

    public PaymentDocumentService(CurrencyService currencyService, 
                                  AccountService accountService, 
                                  PaymentDocumentRepository paymentDocumentRepository, 
                                  ModelMapper mapper) {
        this.currencyService = currencyService;
        this.accountService = accountService;
        this.paymentDocumentRepository = paymentDocumentRepository;
        this.mapper = mapper;
    }

    @PostConstruct
    private void init(){
        TypeMap<PaymentDocument, PaymentDocumentDto> propertyMapper = this.mapper.createTypeMap(PaymentDocument.class, PaymentDocumentDto.class);
        propertyMapper.addMappings(
                el -> el.map(src -> src.getAccount().getAccountNumber(), PaymentDocumentDto::setAccount)
        );
        propertyMapper.addMappings(
                elm -> elm.map(src -> src.getCorrespAccount().getAccountNumber(), PaymentDocumentDto::setCorrespAccount)
        );
    }
    

    public long createDocument(PaymentDocumentDto pDDto) {
        validatePaymentDocument(pDDto);
        PaymentDocument paymentDocument = buildPaymentDocument(pDDto);
        return paymentDocumentRepository.save(paymentDocument).getId();
    }

    @Transactional(rollbackOn = Exception.class)
    public DocumentStatus applyDocument(long id) {
        boolean success;
        PaymentDocument paymentDocument = paymentDocumentRepository.findById(id).orElseThrow(() ->
                new ValidationException(String.format("Payment Document with id %d isn't found", id), NO_ELEMENT));
        try {
            success = accountService.performFundsTransfer(paymentDocument);
            paymentDocument.setDocumentStatus((success) ? DocumentStatus.APPLIED : DocumentStatus.REJECTED);
        } catch (Exception e){ 
            if (!Objects.isNull(paymentDocument)) {
                paymentDocument.setDocumentStatus(DocumentStatus.REJECTED);
                log.error("Error while applying payment document '{}'", id, e);
            }
        } finally {
            if (!Objects.isNull(paymentDocument)) {
                paymentDocument = paymentDocumentRepository.save(paymentDocument);
            }
        }
        return paymentDocument.getDocumentStatus();
    }
    

    public PaymentDocumentDto findById(long id) {
        PaymentDocument paymentDocument = paymentDocumentRepository.findById(id).orElseThrow(() -> new NoElementFoundException(String.format("Document with id %d is not found", id),NO_ELEMENT));
        return mapper.map(paymentDocument, PaymentDocumentDto.class);
    }

    public List<PaymentDocumentDto> findDocumentsByAccount(String accountNumber) {
        return paymentDocumentRepository.findPaymentDocumentsByAccount_AccountNumber(accountNumber).orElse(new ArrayList<>())
                .stream()
                .map(document -> mapper.map(document, PaymentDocumentDto.class))
                .toList();
    }

    private void validatePaymentDocument(PaymentDocumentDto pDDto) {
        if (NullChecker.anyNullExceptOne(pDDto, "documentStatus")) {
            throw new ValidationException("Payment document could have all fields as non null, except status", EMPTY_FIELD);
        }
        if (pDDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount in payment document should be greater then Zero", AMOUNT_SHOULD_BE_GREATER_THEN_ZERO);
        }
    }
    private PaymentDocument buildPaymentDocument(PaymentDocumentDto pDDto) {
        OperationType operationType = OperationType.getOperationType(pDDto.getOperationType());
        Currency currency = currencyService.findCurrencyByShortNameEqualsIgnoreCase(pDDto.getCurrency());
        Account account = accountService.getByAccountNumber(pDDto.getAccount());
        Account correspAccount = accountService.getByAccountNumber(pDDto.getCorrespAccount());
        return PaymentDocument.builder()
                .operationType(operationType)
                .account(account)
                .correspAccount(correspAccount)
                .amount(pDDto.getAmount())
                .currency(currency)
                .documentStatus(DocumentStatus.NEW)
                .createdAt(new Date())
                .build();
    }
}
