package com.task.repository;

import com.task.model.Operation;
import com.task.model.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountOperationRepository extends JpaRepository<Operation, Long> {
    
    Optional<List<Operation>> findOperationsByAccount_AccountNumber(String accountNumber);

    Optional<List<Operation>> findOperationsByAccount_AccountNumberAndOperationType(String accountNumber, OperationType operationType);

}
