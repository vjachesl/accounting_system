package com.task.domain;

import com.task.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDto {

    private String companyName;

    private Integer companyCode;

    private List<Address> addressList;

    private String email;

    private String phone;
}
