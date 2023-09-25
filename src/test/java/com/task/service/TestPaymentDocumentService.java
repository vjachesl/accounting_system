package com.task.service;

import com.task.domain.PaymentDocumentDto;
import com.task.exception.NoElementFoundException;
import com.task.exception.ValidationException;
import com.task.model.PaymentDocument;
import com.task.repository.PaymentDocumentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static com.task.TestObjectHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestPaymentDocumentService {
    @Mock
    CurrencyService currencyService;
    @Mock
    AccountService accountService;
    @Mock
    PaymentDocumentRepository paymentDocumentRepository;
    @Mock
    ModelMapper mapper;
    @InjectMocks
    PaymentDocumentService service;

    @Test
    public void testDocumentCreation() {
        PaymentDocumentDto paymentDocumentDto = getNormalPaymentDocumentDto();
        when(currencyService.findCurrencyByShortNameEqualsIgnoreCase(UAH)).thenReturn(getUahCurrency());
        when(accountService.getByAccountNumber(ACCOUNT_NORMAL)).thenReturn(getNormalUAHAccount());
        when(accountService.getByAccountNumber(ACCOUNT_CORRESP)).thenReturn(getCorrespUAHAccount());
        when(paymentDocumentRepository.save(any(PaymentDocument.class))).thenReturn(getNormalPaymentDocument());

        assertEquals(1L, service.createDocument(paymentDocumentDto));
    }

    @Test
    void testDocumentCreation_ACCOUNT_IS_NULL() {
        PaymentDocumentDto paymentDocumentDto = getNormalPaymentDocumentDto();
        paymentDocumentDto.setAccount(null);

        Throwable throwable = assertThrows(Throwable.class, () -> service.createDocument(paymentDocumentDto));
        assertEquals(ValidationException.class, throwable.getClass());
        assertEquals("Payment document could have all fields as non null, except status", throwable.getMessage());
    }

    @Test
    void testDocumentCreation_AMOUNT_IS_NEGATIVE() {
        PaymentDocumentDto paymentDocumentDto = getNormalPaymentDocumentDto();
        paymentDocumentDto.setAmount(BigDecimal.valueOf(-1L));

        Throwable throwable = assertThrows(Throwable.class, () -> service.createDocument(paymentDocumentDto));
        assertEquals(ValidationException.class, throwable.getClass());
        assertEquals("Amount in payment document should be greater then Zero", throwable.getMessage());
    }

    @Test
    void TestDocumentMissingWhenGetById() {
        when(paymentDocumentRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(Throwable.class, () -> service.findById(1L));
        assertEquals(NoElementFoundException.class, throwable.getClass());
        assertEquals("Document with id 1 is not found", throwable.getMessage());
    }
}
