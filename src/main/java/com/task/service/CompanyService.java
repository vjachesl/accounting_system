package com.task.service;

import com.task.domain.CompanyDto;
import com.task.exception.ErrorCodes;
import com.task.exception.NoElementFoundException;
import com.task.exception.ValidationException;
import com.task.model.Address;
import com.task.model.Company;
import com.task.repository.CompanyRepository;
import com.task.util.ValidatorWrapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.task.exception.ErrorCodes.NO_ELEMENT;

@Service
@Slf4j
public class CompanyService {
    CompanyRepository companyRepository;
    ModelMapper mapper;

    public CompanyService(CompanyRepository companyRepository, 
                          ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }
    @PostConstruct
    private void init(){
        TypeMap<Company, CompanyDto> propertyMapper = this.mapper.createTypeMap(Company.class, CompanyDto.class);
         propertyMapper.addMappings(
                el -> el.map(Company::getAddressList, CompanyDto::setAddressList)
        );
        
    }

    public List<CompanyDto> findAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(elem ->mapper.map(elem, CompanyDto.class)).toList();
    }
    
    public CompanyDto findById(long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new NoElementFoundException(String.format("Company with Id %d doesn't exits", id), NO_ELEMENT));
        return mapper.map(company, CompanyDto.class);
    }

    public  CompanyDto findByCompanyCode(Integer companyCode){
        Company company = companyRepository.findCompanyByCompanyCode(companyCode).orElseThrow(() -> new NoElementFoundException(String.format("Company with code %d doesn't exits", companyCode),NO_ELEMENT));
        return mapper.map(company, CompanyDto.class);
    }
    public  Company getByCompanyCode(Integer companyCode){
        return companyRepository.findCompanyByCompanyCode(companyCode).orElseThrow(() -> new NoElementFoundException(String.format("Company with code %d doesn't exits", companyCode),NO_ELEMENT));
    }

    private boolean isCompanyAlreadyExists(Integer companyCode){
        return companyRepository.existsCompanyByCompanyCode(companyCode);
    }    
    
    public long createCompany(CompanyDto companyDto){
        if(!ValidatorWrapper.isCompanyNameValid(companyDto.getCompanyName())){
            throw new ValidationException("Company name couldn't be empty",ErrorCodes.EMPTY_COMPANY_NAME_FIELD);
        }
        if(!ValidatorWrapper.isCompanyCodeValid(companyDto.getCompanyCode())){
            throw new ValidationException(String.format("Company code %d isn't valid", companyDto.getCompanyCode()),ErrorCodes.NOT_VALID_COMPANY_CODE);
        }
        if (!ValidatorWrapper.isEmailValid(companyDto.getEmail())){
            throw new ValidationException(String.format("Email %s isn't valid",companyDto.getEmail()), ErrorCodes.NON_VALID_EMAIL);
        }
        
        if (isCompanyAlreadyExists(companyDto.getCompanyCode())){
            throw new ValidationException(String.format("Company with code %d already exists", companyDto.getCompanyCode()),  ErrorCodes.DUPLICATE_COMPANY);
        }
        //TODO - introduce method or update with mapper
        List<Address> addressList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(companyDto.getAddressList())){
            addressList = companyDto.getAddressList().stream().map(addressDto -> mapper.map(addressDto, Address.class)).toList();
        }
        Company company = Company.builder()
                .companyName(companyDto.getCompanyName())
                .companyCode(companyDto.getCompanyCode())
                .email(companyDto.getEmail())
                .phone(companyDto.getPhone())
                .createdAt(new Date())
                .addressList(addressList)
                .build();
        
        return companyRepository.save(company).getId();
    }  
   
    //TODO - Provide implementation
    public CompanyDto updateCompanyData(CompanyDto companyDto){
        return null;
    }
}
