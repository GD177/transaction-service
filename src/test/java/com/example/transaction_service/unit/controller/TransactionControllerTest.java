package com.example.transaction_service.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.transaction_service.controller.TransactionController;
import com.example.transaction_service.dto.request.PayInstallmentRequestDTO;
import com.example.transaction_service.dto.request.TransactionRequestDTO;
import com.example.transaction_service.dto.response.TransactionResponseDTO;
import com.example.transaction_service.enums.OperationType;
import com.example.transaction_service.exception.InvalidRequestException;
import com.example.transaction_service.exception.ResourceNotFoundException;
import com.example.transaction_service.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransaction_Success() throws Exception {
        // Arrange
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionId(1L);
        responseDTO.setMessage("Transaction created successfully");

        when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"amount\": 100, \"operationTypeId\": 1}"))
                .andExpect(status().is(CREATED.value()));

        verify(transactionService).createTransaction(any(TransactionRequestDTO.class)); // Verify service call
    }

    @Test
    void testCreateTransaction_Failure() throws Exception {
        // Arrange
        when(transactionService.createTransaction(any(TransactionRequestDTO.class)))
                .thenThrow(new InvalidRequestException("Transaction amount must be greater than zero."));

        // Act & Assert
        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"amount\": -100, \"operationTypeId\": 1}"))
                .andExpect(status().isBadRequest()); // Expect 400 Bad Request
    }

    @Test
    void testPayInstallment_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/transactions/installments/pay")
                        .contentType("application/json")
                        .content("{\"transactionId\": 1, \"installmentNumber\": 1, \"accountId\": 1, \"amount\": 100}"))
                .andExpect(status().is(OK.value())); // Expect 200 OK

        // Verify service call
        verify(transactionService).payInstallmentByNumber(any(PayInstallmentRequestDTO.class));
    }

    @Test
    void testPayInstallment_Failure() throws Exception {

        // Arrange
        PayInstallmentRequestDTO payInstallmentRequestDTO = new PayInstallmentRequestDTO();
        payInstallmentRequestDTO.setTransactionId(1L);
        payInstallmentRequestDTO.setInstallmentNumber(100);
        payInstallmentRequestDTO.setAccountId(19L);
        payInstallmentRequestDTO.setAmount(BigDecimal.valueOf(100));

        doThrow(new ResourceNotFoundException("Installment not found"))
                .when(transactionService).payInstallmentByNumber(any(PayInstallmentRequestDTO.class));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                transactionController.payInstallment(payInstallmentRequestDTO));

        assertEquals("Installment not found", exception.getMessage());

        // Verify service call
        verify(transactionService).payInstallmentByNumber(any(PayInstallmentRequestDTO.class));
    }
}

