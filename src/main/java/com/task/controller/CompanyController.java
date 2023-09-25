package com.task.controller;

import com.task.service.CompanyService;
import com.task.domain.CompanyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return new ResponseEntity<>(companyService.findAllCompanies(), HttpStatus.OK);
    }    
    
    @GetMapping("/{code}")
    public ResponseEntity<CompanyDto> getCompanyByCode(@PathVariable("code") Integer companyCode) {
        return new ResponseEntity<>(companyService.findByCompanyCode(companyCode), HttpStatus.OK);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") long id) {
        return new ResponseEntity<>(companyService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> createCompany(@RequestBody CompanyDto companyDto) {
            return new ResponseEntity<>(companyService.createCompany(companyDto), HttpStatus.CREATED);
      }
}
