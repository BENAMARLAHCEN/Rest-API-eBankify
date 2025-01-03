package com.banking.restapiebankify.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Generic error occurred");

        ResponseEntity<?> response = exceptionHandler.handleException(exception);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Generic error occurred", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void handleBadCredentials_ShouldReturnUnauthorized() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        ResponseEntity<?> response = exceptionHandler.handleBadCredentials(exception);

        assertNotNull(response);
        assertEquals(401, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Invalid credentials", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<?> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Invalid argument", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void handleBankAccountNotFoundException_ShouldReturnNotFound() {
        BankAccountNotFoundException exception = new BankAccountNotFoundException("Bank account not found");

        ResponseEntity<?> response = exceptionHandler.handleBankAccountNotFoundException(exception);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Bank account not found", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void handleTransactionNotFoundException_ShouldReturnNotFound() {
        TransactionNotFoundException exception = new TransactionNotFoundException("Transaction not found");

        ResponseEntity<?> response = exceptionHandler.handleTransactionNotFoundException(exception);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Transaction not found", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void handleUnauthorizedAccessException_ShouldReturnForbidden() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException("Access denied");

        ResponseEntity<?> response = exceptionHandler.handleUnauthorizedAccessException(exception);

        assertNotNull(response);
        assertEquals(403, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertEquals("Access denied", ((Map<?, ?>) response.getBody()).get("error"));
    }
}