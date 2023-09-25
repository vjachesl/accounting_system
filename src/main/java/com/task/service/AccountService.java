package com.task.service;

import com.task.domain.AccountDto;
import com.task.domain.OperationDto;
import com.task.exception.ErrorCodes;
import com.task.exception.NoElementFoundException;
import com.task.exception.ValidationException;
import com.task.model.*;
import com.task.model.Currency;
import com.task.model.enums.OperationType;
import com.task.repository.AccountOperationRepository;
import com.task.repository.AccountRepository;
import com.task.util.NullChecker;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.task.exception.ErrorCodes.*;
import static com.task.model.enums.OperationType.CREDIT;
import static com.task.model.enums.OperationType.DEBIT;


@Service
public class AccountService {
    public static final String ACCOUNT_WITH_ACCOUNT_NUMBER_S_IS_NOT_FOUND = "Account with accountNumber %s is not found";
    AccountRepository accountRepository;
    AccountOperationRepository accountOperationRepository;
    CompanyService companyService;
    
    CurrencyService currencyService;
    ModelMapper mapper;

    public AccountService(AccountRepository accountRepository,
                          AccountOperationRepository accountOperationRepository,
                          CompanyService companyService,
                          CurrencyService currencyService,
                          ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.currencyService = currencyService;
        this.companyService = companyService;
        this.mapper = mapper;
    }

    @PostConstruct
    private void init() {
        TypeMap<Operation, OperationDto> propertyMapper = this.mapper.createTypeMap(Operation.class, OperationDto.class);
        propertyMapper.addMappings(
                el -> el.map(src -> src.getAccount().getAccountNumber(), OperationDto::setAccount)
        );
        propertyMapper.addMappings(
                elm -> elm.map(src -> src.getCorrespAccount().getAccountNumber(), OperationDto::setCorrespAccount)
        );
    }

    public long createAccount(AccountDto accountDto) {
        if (NullChecker.anyNull(accountDto)) {
            throw new ValidationException("Fields of new account should be not null", EMPTY_FIELD);
        }
        if (accountRepository.existsAccountByAccountNumberEqualsIgnoreCase(accountDto.getAccountNumber())) {
            throw new ValidationException(String.format("Account with the number %s already exists. Please use update.", accountDto.getAccountNumber()), ErrorCodes.DUPLICATE_ACCOUNT);
        }
        Account account = mapper.map(accountDto, Account.class);
        Company company = companyService.getByCompanyCode(accountDto.getCompanyCode());
        account.setCompany(company);
        Currency currency = currencyService.findCurrencyByShortNameEqualsIgnoreCase(accountDto.getCurrency());
        account.setCurrency(currency);
        return accountRepository.save(account).getId();
    }

    public AccountDto findById(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoElementFoundException(String.format("Account with id %d is not found", id), NO_ELEMENT));
        return mapper.map(account, AccountDto.class);
    }

    public AccountDto findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findAccountByAccountNumberEqualsIgnoreCase(accountNumber)
                .orElseThrow(() -> new NoElementFoundException(String.format(ACCOUNT_WITH_ACCOUNT_NUMBER_S_IS_NOT_FOUND, accountNumber), NO_ELEMENT));
        return mapper.map(account, AccountDto.class);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumberEqualsIgnoreCase(accountNumber)
                .orElseThrow(() -> new NoElementFoundException(String.format(ACCOUNT_WITH_ACCOUNT_NUMBER_S_IS_NOT_FOUND, accountNumber), NO_ELEMENT));
    }

    public List<AccountDto> findByCompanyCode(Integer companyCode) {
        return accountRepository.findAccountsByCompany_Id(companyService.getByCompanyCode(companyCode).getId())
                .orElse(new ArrayList<>())
                .stream()
                .map(account -> mapper.map(account, AccountDto.class))
                .toList();
    }

    public List<OperationDto> findAccountOperationsByAccountNumber(String accountNumber) {
        if (!accountRepository.existsAccountByAccountNumberEqualsIgnoreCase(accountNumber)) {
            throw new NoElementFoundException(String.format(ACCOUNT_WITH_ACCOUNT_NUMBER_S_IS_NOT_FOUND, accountNumber), NO_ELEMENT);
        }
        Optional<List<Operation>> operationList = accountOperationRepository.findOperationsByAccount_AccountNumber(accountNumber);
        return CollectionUtils.isEmpty(operationList.get()) ? new ArrayList<>() : operationList.get().stream().map(operation -> mapper.map(operation, OperationDto.class)).toList();
    }

    public List<OperationDto> findAccountOperationsByAccountNumberAndOperationType(String accountNumber, String inOperationType) {
        OperationType operationType = OperationType.getOperationType(inOperationType);
        Optional<List<Operation>> operationList = accountOperationRepository.findOperationsByAccount_AccountNumberAndOperationType(accountNumber, operationType);
        return CollectionUtils.isEmpty(operationList.get()) ? new ArrayList<>() : mapper.map(operationList.get(), List.class);
    }

    public boolean performFundsTransfer(PaymentDocument pD) {
        //TODO We could add multi currency operations with applying currencyExchange service;
        // For now, we didn't allow operations with multi currencies;
        if (!pD.getAccount().getCurrency().equals(pD.getCurrency()) || !pD.getCorrespAccount().getCurrency().equals(pD.getCurrency())) {
            throw new ValidationException("Currencies in destination and source accounts should be the same as in operation", CURRENCIES_MUST_BE_EQUAL);
        }
        return createOperations(pD);
    }

    private boolean createOperations(PaymentDocument paymentDocument) {
        OperationType operationType = paymentDocument.getOperationType();
        switch (operationType) {
            case CREDIT -> {
                if (paymentDocument.getCorrespAccount().getAmount().compareTo(paymentDocument.getAmount()) < 0) {
                    throw new ValidationException(String.format("Account %s has not enough money to complete the operation", paymentDocument.getCorrespAccount().getAccountNumber()), NOT_ENOUGH_MONEY_ON_ACCOUNT);
                } else {
                    Account correspondentAccount = paymentDocument.getCorrespAccount();
                    Account account = paymentDocument.getAccount();
                    correspondentAccount.setAmount(correspondentAccount.getAmount().subtract(paymentDocument.getAmount()));
                    account.setAmount(account.getAmount().add(paymentDocument.getAmount()));
                    accountRepository.saveAll(Arrays.asList(account, correspondentAccount));
                    Operation operation = Operation.builder()
                            .operationType(CREDIT)
                            .paymentDocument(paymentDocument)
                            .account(account)
                            .correspAccount(correspondentAccount)
                            .creditAmount(paymentDocument.getAmount())
                            .debitAmount(null)
                            .currency(paymentDocument.getCurrency())
                            .createdAt(new Date())
                            .build();

                    Operation mirrorOperation = Operation.builder()
                            .operationType(DEBIT)
                            .paymentDocument(paymentDocument)
                            .account(correspondentAccount)
                            .correspAccount(account)
                            .creditAmount(null)
                            .debitAmount(paymentDocument.getAmount())
                            .currency(paymentDocument.getCurrency())
                            .createdAt(new Date())
                            .build();
                    accountOperationRepository.saveAll(Arrays.asList(operation, mirrorOperation));
                }
            }
            case DEBIT -> {
                if (paymentDocument.getAccount().getAmount().compareTo(paymentDocument.getAmount()) < 0) {
                    throw new ValidationException(String.format("Account %s has not enough money to complete the operation", paymentDocument.getCorrespAccount().getAccountNumber()), NOT_ENOUGH_MONEY_ON_ACCOUNT);
                } else {
                    Account correspondentAccount = paymentDocument.getCorrespAccount();
                    Account account = paymentDocument.getAccount();
                    account.setAmount(account.getAmount().subtract(paymentDocument.getAmount()));
                    correspondentAccount.setAmount(correspondentAccount.getAmount().add(paymentDocument.getAmount()));
                    accountRepository.saveAll(Arrays.asList(account, correspondentAccount));
                    Operation operation = Operation.builder()
                            .operationType(DEBIT)
                            .paymentDocument(paymentDocument)
                            .account(account)
                            .correspAccount(correspondentAccount)
                            .debitAmount(paymentDocument.getAmount())
                            .creditAmount(null)
                            .currency(paymentDocument.getCurrency())
                            .createdAt(new Date())
                            .build();

                    Operation mirrorOperation = Operation.builder()
                            .operationType(CREDIT)
                            .paymentDocument(paymentDocument)
                            .account(correspondentAccount)
                            .correspAccount(account)
                            .creditAmount(paymentDocument.getAmount())
                            .debitAmount(null)
                            .currency(paymentDocument.getCurrency())
                            .createdAt(new Date())
                            .build();
                    accountOperationRepository.saveAll(Arrays.asList(operation, mirrorOperation));
                }
            }
        }
        return true;
    }

}
