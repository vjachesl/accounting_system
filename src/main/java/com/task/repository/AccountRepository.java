package com.task.repository;

import com.task.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<Account>> findAccountsByCompany_Id(long id);

    Optional<Account> findAccountByAccountNumberEqualsIgnoreCase(String accountNumber);

    boolean existsAccountByAccountNumberEqualsIgnoreCase(String accountNumber);

}
