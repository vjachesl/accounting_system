package com.task.service;

import com.task.exception.ErrorCodes;
import com.task.exception.ValidationException;
import com.task.model.Currency;
import com.task.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyService {

    CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    Currency findCurrencyByShortNameEqualsIgnoreCase(String shortName) {
        return currencyRepository.findCurrencyByShortNameEqualsIgnoreCase(shortName)
                .orElseThrow(()-> new ValidationException(String.format("No currency available for the next %s value", shortName), ErrorCodes.INVALID_CURRENCY));
    }
    Currency createCurrency(Currency currency){
        return currencyRepository.save(currency);
    }
}
