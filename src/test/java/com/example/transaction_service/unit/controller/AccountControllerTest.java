package com.example.transaction_service.unit.controller;

import com.example.transaction_service.controller.AccountController;
import com.example.transaction_service.dto.request.AccountRequestDTO;
import com.example.transaction_service.entity.Account;
import com.example.transaction_service.exception.GlobalExceptionHandler;
import com.example.transaction_service.exception.ResourceNotFoundException;
import com.example.transaction_service.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;


    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setDocumentNumber("123456789");

        Account account = new Account();
        account.setAccountId(1L);
        account.setDocumentNumber(accountRequestDTO.getDocumentNumber());

        when(accountService.createAccount(any(AccountRequestDTO.class))).thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                        .contentType("application/json")
                        .content("{\"documentNumber\": \"123456789\"}"))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.accountId").value(1L)); // Validate the returned accountId
    }


    @Test
    void testCreateAccount_Failed() throws Exception {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setDocumentNumber("123456789");

        when(accountService.createAccount(any(AccountRequestDTO.class)))
                .thenThrow(new RuntimeException("Failed to create account"));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            accountController.createAccount(accountRequestDTO));

        // Assert
        assertEquals("Failed to create account", exception.getMessage());

        // Verify that createAccount method was called on the accountService
        verify(accountService).createAccount(any(AccountRequestDTO.class));
    }

    @Test
    void testGetAccountById_Success() {
        // Arrange
        Long accountId = 1L;
        Account account = new Account();
        account.setAccountId(accountId);
        account.setDocumentNumber("123456789");

        when(accountService.getAccountById(anyLong())).thenReturn(account);

        // Act
        ResponseEntity<Account> response = accountController.getAccountById(accountId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());

        // Verify service call
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void testGetAccountById_NotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountService.getAccountById(anyLong())).thenThrow(new ResourceNotFoundException("Account not found"));

        // Act & Assert
        try {
            accountController.getAccountById(accountId);
        } catch (ResourceNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }

        // Verify service call
        verify(accountService).getAccountById(accountId);
    }
}
