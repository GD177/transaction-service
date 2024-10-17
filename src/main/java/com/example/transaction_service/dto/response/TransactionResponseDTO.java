package com.example.transaction_service.dto.response;

import java.math.BigDecimal;

public class TransactionResponseDTO {
    private Long transactionId;
    private String message;

    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}

