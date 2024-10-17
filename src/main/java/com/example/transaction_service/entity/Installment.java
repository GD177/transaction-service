package com.example.transaction_service.entity;

import com.example.transaction_service.enums.InstallmentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installments")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;  // References the original purchase transaction

    private Integer installmentNumber;

    private BigDecimal installmentAmount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private InstallmentStatus status;  // PENDING, PAID, etc.

    // Getters and setters
    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setStatus(InstallmentStatus status) {
        this.status = status;
    }

    public void setTransaction(Transaction savedTransaction) {
    }

    public void setInstallmentNumber(int installmentNumber) {
    }

    public void setInstallmentAmount(BigDecimal amount) {
    }
}

