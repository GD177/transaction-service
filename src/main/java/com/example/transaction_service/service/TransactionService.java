package com.example.transaction_service.service;

import com.example.transaction_service.dto.request.InstallmentDTO;
import com.example.transaction_service.dto.request.PayInstallmentRequestDTO;
import com.example.transaction_service.dto.request.TransactionRequestDTO;
import com.example.transaction_service.dto.response.TransactionResponseDTO;
import com.example.transaction_service.entity.Account;
import com.example.transaction_service.entity.Installment;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.enums.InstallmentStatus;
import com.example.transaction_service.exception.InvalidRequestException;
import com.example.transaction_service.exception.ResourceNotFoundException;
import com.example.transaction_service.enums.OperationType;
import com.example.transaction_service.repository.AccountRepository;
import com.example.transaction_service.repository.InstallmentRepository;
import com.example.transaction_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final InstallmentRepository installmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                              InstallmentRepository installmentRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.installmentRepository = installmentRepository;
    }

    @Transactional
    public TransactionResponseDTO createTransaction(final TransactionRequestDTO transactionDTO) {
        // Validate transaction amount
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Transaction amount must be greater than zero, provided: {}", transactionDTO.getAmount());
            throw new InvalidRequestException("Transaction amount must be greater than zero.");
        }

        // Validate operation type ID
        OperationType operationType = validateOperationType(transactionDTO.getOperationTypeId());

        // Adjust the transaction amount based on the operation type
        // Convert to negative for purchases/withdrawals/purchase_with_installment
        BigDecimal adjustedAmount = transactionDTO.getAmount();

        if (operationType == OperationType.NORMAL_PURCHASE || operationType == OperationType.WITHDRAWAL ||
                operationType == OperationType.PURCHASE_INSTALLMENTS) {
            adjustedAmount = transactionDTO.getAmount().negate();
        }

        // Retrieve the account and handle if it does not exist
        Account account = findAccountById(transactionDTO.getAccountId());

        // Create and save transaction
        Transaction transaction = buildTransaction(account, operationType, adjustedAmount);

        if (operationType == OperationType.PURCHASE_INSTALLMENTS) {
            return getTransactionResponseDTO(createTransactionWithInstallments(transaction, transactionDTO), account);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Created transaction with ID: {}", savedTransaction.getTransactionId());

        return getTransactionResponseDTO(savedTransaction, account);
    }

    @Transactional
    public Transaction createTransactionWithInstallments(Transaction transaction, TransactionRequestDTO transactionDTO) {

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Create individual installment records and link them to the transaction
        int installmentNumber = 1;
        for (InstallmentDTO installments : transactionDTO.getInstallments()) {
            Installment installment = new Installment();
            installment.setTransaction(savedTransaction);
            installment.setInstallmentNumber(installmentNumber);
            installment.setInstallmentAmount(installments.getAmount());
            installment.setStatus(InstallmentStatus.PENDING);
            installmentRepository.save(installment);

            installmentNumber++;
        }

        return savedTransaction;
    }

    @Transactional
    public void payInstallmentByNumber(final PayInstallmentRequestDTO payInstallmentRequest) {
        // Step 1: Find the transaction and its corresponding installment
        Transaction transaction = transactionRepository.findById(payInstallmentRequest.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Installment installment = installmentRepository.findByTransactionAndInstallmentNumber(transaction,
                        payInstallmentRequest.getInstallmentNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Installment not found or already paid"));

        // Step 2: Check if the amount matches the installment amount
        if (installment.getInstallmentAmount().compareTo(payInstallmentRequest.getAmount()) != 0) {
            throw new InvalidRequestException("Paid amount does not match the installment amount.");
        }

        // Step 3: Create a new transaction for the installment payment
        Transaction installmentPaymentTransaction = new Transaction();
        installmentPaymentTransaction.setAccount(findAccountById(payInstallmentRequest.getAccountId()));
        installmentPaymentTransaction.setOperationTypeId(OperationType.INSTALLMENT_PAYMENT.getId());
        installmentPaymentTransaction.setAmount(payInstallmentRequest.getAmount());
        transactionRepository.save(installmentPaymentTransaction);

        // Step 4: Mark the installment as paid
        installment.setStatus(InstallmentStatus.PAID);
        installmentRepository.save(installment);
    }

    private TransactionResponseDTO getTransactionResponseDTO(Transaction savedTransaction, Account account) {
        // Map to TransactionResponseDTO
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionId(savedTransaction.getTransactionId());
        responseDTO.setMessage("Transaction created successfully");
        return responseDTO;
    }

    private OperationType validateOperationType(int operationTypeId) {
        try {
            return OperationType.fromId(operationTypeId);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid operation type ID: {}", operationTypeId);
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Account not found with ID: {}", accountId);
                    return new ResourceNotFoundException("Account not found with ID: " + accountId);
                });
    }

    private Transaction buildTransaction(Account account, OperationType operationType, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationTypeId(operationType.getId());
        transaction.setAmount(amount);
        return transaction;
    }
}
