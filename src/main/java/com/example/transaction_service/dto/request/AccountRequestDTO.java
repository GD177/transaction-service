package com.example.transaction_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class AccountRequestDTO {
    @NotBlank(message = "Document number is required")
    @Size(min = 9, max = 12, message = "Document number must be exactly 11 digits")
    private String documentNumber;

    // Getters and setters
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}