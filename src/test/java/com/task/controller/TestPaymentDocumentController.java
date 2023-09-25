package com.task.controller;

import com.task.service.PaymentDocumentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestPaymentDocumentController {
    @Autowired
    private MockMvc mvc;
    @Mock
    private PaymentDocumentService paymentDocumentService;

    @Test
    void getPaymentDocumentDto_ExceptionalFlow() throws Exception {
        when(paymentDocumentService.findById(1L)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/payment-documents/{id} ", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("NO_ELEMENT"));
    }
}
