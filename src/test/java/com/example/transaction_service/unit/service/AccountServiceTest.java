package com.example.transaction_service.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.transaction_service.dto.request.AccountRequestDTO;
import com.example.transaction_service.entity.Account;
import com.example.transaction_service.exception.ResourceNotFoundException;
import com.example.transaction_service.repository.AccountRepository;
import com.example.transaction_service.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountRequestDTO accountRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setDocumentNumber("123456789");
    }

    @Test
    void testCreateAccount_Success() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1L);
        account.setDocumentNumber(accountRequestDTO.getDocumentNumber());

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        Account savedAccount = accountService.createAccount(accountRequestDTO);

        // Assert
        assertNotNull(savedAccount);
        assertEquals(1L, savedAccount.getAccountId());
        assertEquals(accountRequestDTO.getDocumentNumber(), savedAccount.getDocumentNumber());
        verify(accountRepository, times(1)).save(any(Account.class)); // Verify save was called once
    }

    @Test
    void testCreateAccount_Failure() {
        // Arrange
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.createAccount(accountRequestDTO));

        assertEquals("Database error", exception.getMessage());
        verify(accountRepository, times(1)).save(any(Account.class)); // Verify save was called once
    }

    @Test
    void testGetAccountById_Success() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1L);
        account.setDocumentNumber("123456789");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        Account fetchedAccount = accountService.getAccountById(1L);

        // Assert
        assertNotNull(fetchedAccount);
        assertEquals(1L, fetchedAccount.getAccountId());
        verify(accountRepository, times(1)).findById(1L); // Verify findById was called once
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(1L));

        assertEquals("Account not found with ID: 1", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L); // Verify findById was called once
    }
}
