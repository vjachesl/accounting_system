package com.task.controller;

import com.task.domain.PaymentDocumentDto;
import com.task.model.enums.DocumentStatus;
import com.task.service.PaymentDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payment-Document-API", description = "the Api for deal with payment documents")
@RestController
@RequestMapping("/api/payment-documents")
public class PaymentDocumentController {

    PaymentDocumentService paymentDocumentService;

    public PaymentDocumentController(PaymentDocumentService paymentDocumentService) {
        this.paymentDocumentService = paymentDocumentService;
    }
    @Operation(summary = "Create the payment document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns an id of created document", content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "When input payload isn't pass the validation process", content = @Content) })
    @PostMapping("/")
    public ResponseEntity<Long> createPaymentDocument(@RequestBody PaymentDocumentDto paymentDocumentDto) {
        return new ResponseEntity<>(paymentDocumentService.createDocument(paymentDocumentDto), HttpStatus.CREATED);
    }

    @GetMapping("/apply/{id}")
    public ResponseEntity<DocumentStatus> applyPaymentDocument(@PathVariable("id") long id) {
        return new ResponseEntity<>(paymentDocumentService.applyDocument(id), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDocumentDto> getDocumentById(@PathVariable("id") long id) {
        return new ResponseEntity<>(paymentDocumentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("accountNumber/{account-number}")
    public ResponseEntity<List<PaymentDocumentDto>> getDocumentByAccountNumber(@PathVariable("account-number") String accountNumber) {
        return new ResponseEntity<>(paymentDocumentService.findDocumentsByAccount(accountNumber), HttpStatus.OK);
    }
}
