// exception/GlobalExceptionHandler.java
package com.agilesolutions.card.exception;

import com.agilesolutions.card.domain.dto.ApiResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler mapping Java exceptions to COBOL error paths:
 *
 *   CardNotFoundException        -> COBOL FILE STATUS '23'  -> HTTP 404
 *   BusinessValidationException  -> COBOL SEND-ERRMSG       -> HTTP 400
 *   AccessDeniedException        -> COBOL CICS NOTAUTH      -> HTTP 403
 *   BadCredentialsException      -> COBOL VERIFY PASSWORD   -> HTTP 401
 *   OptimisticLockException      -> COBOL FILE STATUS '09'  -> HTTP 409
 *   DataIntegrityViolation       -> COBOL FILE STATUS '22'  -> HTTP 409
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // COBOL: FILE STATUS '23'
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleCardNotFound(
            CardNotFoundException ex) {
        log.warn("Card not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDto.error(ex.getMessage(), ex.getErrorCode()));
    }

    // COBOL: SEND-ERRMSG
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBusinessValidation(
            BusinessValidationException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(
                        ex.getMessage(), ex.getErrorCode(),
                        ex.getValidationErrors()));
    }

    // COBOL: CICS NOTAUTH
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAccessDenied(
            AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponseDto.error("Access denied", "ERR_FORBIDDEN"));
    }

    // COBOL: VERIFY PASSWORD RESP(ERROR)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadCredentials(
            BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDto.error(
                        "Invalid username or password", "ERR_UNAUTHORIZED"));
    }

    // COBOL: FILE STATUS '09'
    @ExceptionHandler({
        OptimisticLockException.class,
        jakarta.persistence.OptimisticLockException.class,
        org.springframework.orm.ObjectOptimisticLockingFailureException.class
    })
    public ResponseEntity<ApiResponseDto<Void>> handleOptimisticLock(
            Exception ex) {
        log.warn("Optimistic lock conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDto.error(
                        "Record modified by another user. Please refresh and retry.",
                        "ERR_CONCURRENT_UPDATE"));
    }

    // COBOL: FILE STATUS '22'
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleDataIntegrity(
            DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDto.error(
                        "Card already exists or constraint violated",
                        "ERR_CARD_EXISTS"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(
                ApiResponseDto.error("Request validation failed",
                        "ERR_VALIDATION_FAILED", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(
                        "Constraint violation",
                        "ERR_VALIDATION_FAILED", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error(
                        "An unexpected error occurred", "ERR_INTERNAL"));
    }
}