package com.task.repository;

import com.task.model.PaymentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentDocumentRepository  extends JpaRepository<PaymentDocument, Long> {
    Optional<List<PaymentDocument>> findPaymentDocumentsByAccount_AccountNumber(String accountNumber);
}
