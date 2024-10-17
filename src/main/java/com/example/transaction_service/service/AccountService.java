package com.example.transaction_service.service;

import com.example.transaction_service.dto.request.AccountRequestDTO;
import com.example.transaction_service.entity.Account;
import com.example.transaction_service.exception.ResourceNotFoundException;
import com.example.transaction_service.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(final AccountRequestDTO accountDTO) {
        Account account = new Account();
        account.setDocumentNumber(accountDTO.getDocumentNumber());

        Account savedAccount = accountRepository.save(account);
        logger.info("Created account with ID: {}", savedAccount.getAccountId());
        return savedAccount;
    }

    public Account getAccountById(final Long accountId) {
        logger.info("Fetching account with ID: {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Account not found with ID: {}", accountId);
                    return new ResourceNotFoundException("Account not found with ID: " + accountId);
                });
    }
}
