package com.task.repository;

import com.task.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findCurrencyByShortNameEqualsIgnoreCase(String shortName);

}
