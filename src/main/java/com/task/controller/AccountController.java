package com.task.controller;

import com.task.domain.AccountDto;
import com.task.domain.OperationDto;
import com.task.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") long id) {
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/number/{account-number}")
    public ResponseEntity<AccountDto> getAccountByNumber(@PathVariable("account-number") String accountNumber) {
        return new ResponseEntity<>(accountService.findByAccountNumber(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/number/{account-number}/operations")
    public ResponseEntity<List<OperationDto>> getAccountOperationsByAccountNumber(@PathVariable("account-number") String accountNumber) {
        return new ResponseEntity<>(accountService.findAccountOperationsByAccountNumber(accountNumber), HttpStatus.OK);
    }
    
    @GetMapping("/{account-number}/operations/{operation-type}")
    public ResponseEntity<List<OperationDto>> getAccountOperationsByAccountNumber(@PathVariable("account-number") String accountNumber, @PathVariable("operation-type") String operationType ) {
        return new ResponseEntity<>(accountService.findAccountOperationsByAccountNumberAndOperationType(accountNumber, operationType), HttpStatus.OK);
    }
    
    @GetMapping("/company-code/{company-code}")
    public ResponseEntity<List<AccountDto>> getAccountsByCompanyCode(@PathVariable("company-code") Integer companyCode) {
        return new ResponseEntity<>(accountService.findByCompanyCode(companyCode), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }
}
