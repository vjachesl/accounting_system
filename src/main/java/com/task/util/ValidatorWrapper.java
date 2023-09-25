package com.task.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class ValidatorWrapper {

    private ValidatorWrapper() {
    }

    public static boolean isEmailValid(String email){
        return  EmailValidator.getInstance().isValid(email);
    }

    public static boolean isCompanyNameValid(String companyName){
        return StringUtils.isNoneBlank(companyName);
    }

    public static boolean isCompanyCodeValid(Integer companyCode){
       if (companyCode <= 0){
           return false;
       }
       int length = (int)(Math.log10(companyCode)+1);
       return length==8 || length==10;
    }
}
