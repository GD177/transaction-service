package com.example.transaction_service.controller;

import com.example.transaction_service.dto.request.PayInstallmentRequestDTO;
import com.example.transaction_service.dto.request.TransactionRequestDTO;
import com.example.transaction_service.dto.response.TransactionResponseDTO;
import com.example.transaction_service.service.TransactionService;
import com.example.transaction_service.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody final TransactionRequestDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @PostMapping("/transactions/installments/pay")
    public void payInstallment(@Valid @RequestBody final PayInstallmentRequestDTO payInstallmentRequestDTO) {
        transactionService.payInstallmentByNumber(payInstallmentRequestDTO);

        ApiResponse<Void> response = new ApiResponse<>(
                "Installment payment successful",
                null
        );

        new ResponseEntity<>(response, HttpStatus.OK);
    }
}
