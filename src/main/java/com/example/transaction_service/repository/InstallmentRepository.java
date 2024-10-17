package com.example.transaction_service.repository;

import com.example.transaction_service.entity.Installment;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.enums.InstallmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByTransactionAndStatus(Transaction transaction, InstallmentStatus status);
    Optional<Installment> findByTransactionAndInstallmentNumber(Transaction transaction, Integer installmentNumber);
}

