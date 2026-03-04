package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.dto.CardRequestDto;
import com.agilesolutions.card.domain.dto.CardUpdateDto;
import com.agilesolutions.card.domain.entity.Card;
import com.agilesolutions.card.exception.BusinessValidationException;
import com.agilesolutions.card.repository.CardAccountRepository;
import com.agilesolutions.card.util.CardConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation service refactored from COBOL paragraphs:
 *
 *   EDIT-CARD-DATA
 *     EDIT-CARDNUM            -> validateCardNum()
 *     EDIT-ACCTID             -> validateAcctId()
 *     VALIDATE-ACCOUNT-DATA   -> validateAccountExists()
 *     EDIT-EMBOSSED-NAME      -> validateEmbossedName()
 *     EDIT-CREDIT-LIMIT       -> validateCreditLimits()
 *     EDIT-EXPIRY-DATE        -> validateDates()
 *     EDIT-REISSUE-DATE       -> validateDates()
 *
 * COBOL used WS-ERROR-FLAGS -> collected List<String> -> BusinessValidationException
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final CardAccountRepository cardAccountRepository;

    /**
     * Full validation on CREATE.
     * COBOL: EDIT-CARD-DATA paragraph (full path).
     */
    public void validateCardData(CardRequestDto dto) {
        List<String> errors = new ArrayList<>();

        validateCardNum(dto.getCardNum(), errors);
        validateAcctId(dto.getCardAcctId(), errors);
        validateAccountExists(dto.getCardAcctId(), errors);
        validateEmbossedName(dto.getCardEmbossedName(), errors);
        validateCreditLimits(dto.getCreditLimit(), dto.getCashCreditLimit(), errors);
        validateDates(dto.getOpenDate(), dto.getExpiryDate(), dto.getReissueDate(), errors);

        throwIfErrors(errors);
    }

    /**
     * Partial validation on UPDATE.
     * COBOL: EDIT-CARD-DATA (modified fields only).
     */
    public void validateUpdateData(CardUpdateDto dto, Card existing) {
        List<String> errors = new ArrayList<>();

        if (dto.getCreditLimit() != null && dto.getCashCreditLimit() != null) {
            validateCreditLimits(dto.getCreditLimit(), dto.getCashCreditLimit(), errors);
        }

        if (dto.getExpiryDate() != null) {
            LocalDate ref = existing.getOpenDate() != null
                    ? existing.getOpenDate() : LocalDate.now();
            if (dto.getExpiryDate().isBefore(ref)) {
                errors.add("Expiry date must be after open date");
            }
        }

        if (dto.getReissueDate() != null && dto.getExpiryDate() != null
                && dto.getReissueDate().isAfter(dto.getExpiryDate())) {
            errors.add("Reissue date must be before expiry date");
        }

        throwIfErrors(errors);
    }

    // ─── Private sub-paragraph validators ────────────────────────────────────

    /**
     * COBOL: EDIT-CARDNUM
     *   IF CARD-NUM IS NOT NUMERIC OR LENGTH NOT = 16
     *     MOVE 'Y' TO WS-CARDNUM-ERROR-FLG
     */
    private void validateCardNum(String cardNum, List<String> errors) {
        if (cardNum == null || cardNum.isBlank()) {
            errors.add("Card number is required");
            return;
        }
        if (!cardNum.matches("^[0-9]{16}$")) {
            errors.add("Card number must be 16 numeric digits");
        }
    }

    /**
     * COBOL: EDIT-ACCTID
     *   IF CARD-ACCT-ID IS NOT NUMERIC OR LENGTH NOT = 11
     *     MOVE 'Y' TO WS-ACCTID-ERROR-FLG
     */
    private void validateAcctId(String acctId, List<String> errors) {
        if (acctId == null || acctId.isBlank()) {
            errors.add("Account ID is required");
            return;
        }
        if (!acctId.matches("^[0-9]{11}$")) {
            errors.add("Account ID must be 11 numeric digits");
        }
    }

    /**
     * COBOL: VALIDATE-ACCOUNT-DATA
     *   READ ACCTDAT KEY = CARD-ACCT-ID
     *   IF FILE-STATUS NOT = '00'
     *     MOVE 'Y' TO WS-ACCT-NOT-FOUND-FLG
     */
    private void validateAccountExists(String acctId, List<String> errors) {
        if (acctId != null && acctId.matches("^[0-9]{11}$")) {
            if (!cardAccountRepository.existsByAcctId(acctId)) {
                errors.add("Account not found for ID: " + acctId);
            }
        }
    }

    /**
     * COBOL: EDIT-EMBOSSED-NAME
     *   IF CARD-EMBOSSED-NAME = SPACES
     *     MOVE 'Y' TO WS-NAME-ERROR-FLG
     */
    private void validateEmbossedName(String name, List<String> errors) {
        if (name != null && name.trim().isEmpty()) {
            errors.add("Embossed name cannot be blank");
        }
    }

    /**
     * COBOL: EDIT-CREDIT-LIMIT
     *   IF CARD-CASH-CREDIT-LIMIT > CARD-CREDIT-LIMIT
     *     MOVE 'Y' TO WS-LIMIT-ERROR-FLG
     */
    private void validateCreditLimits(
            java.math.BigDecimal creditLimit,
            java.math.BigDecimal cashCreditLimit,
            List<String> errors) {

        if (creditLimit == null || cashCreditLimit == null) return;

        // COBOL: IF CARD-CASH-CREDIT-LIMIT > CARD-CREDIT-LIMIT
        //          MOVE 'Y' TO WS-LIMIT-ERROR-FLG
        if (cashCreditLimit.compareTo(creditLimit) > 0) {
            errors.add("Cash credit limit cannot exceed total credit limit");
        }

        // COBOL: IF CARD-CREDIT-LIMIT < ZERO
        //          MOVE 'Y' TO WS-LIMIT-ERROR-FLG
        if (creditLimit.compareTo(java.math.BigDecimal.ZERO) < 0) {
            errors.add("Credit limit cannot be negative");
        }

        if (cashCreditLimit.compareTo(java.math.BigDecimal.ZERO) < 0) {
            errors.add("Cash credit limit cannot be negative");
        }
    }

    /**
     * COBOL: EDIT-EXPIRY-DATE / EDIT-REISSUE-DATE
     *   IF CARD-EXPIRAION-DATE < CARD-OPEN-DATE
     *     MOVE 'Y' TO WS-DATE-ERROR-FLG
     *   IF CARD-REISSUE-DATE > CARD-EXPIRAION-DATE
     *     MOVE 'Y' TO WS-DATE-ERROR-FLG
     */
    private void validateDates(
            LocalDate openDate,
            LocalDate expiryDate,
            LocalDate reissueDate,
            List<String> errors) {

        LocalDate effectiveOpen = openDate != null ? openDate : LocalDate.now();

        // COBOL: IF CARD-OPEN-DATE > FUNCTION CURRENT-DATE
        if (openDate != null && openDate.isAfter(LocalDate.now())) {
            errors.add("Open date cannot be in the future");
        }

        // COBOL: IF CARD-EXPIRAION-DATE < CARD-OPEN-DATE
        if (expiryDate != null && expiryDate.isBefore(effectiveOpen)) {
            errors.add("Expiry date must be after open date");
        }

        // COBOL: IF CARD-EXPIRAION-DATE < FUNCTION CURRENT-DATE
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            errors.add("Expiry date cannot be in the past");
        }

        // COBOL: IF CARD-REISSUE-DATE > CARD-EXPIRAION-DATE
        if (reissueDate != null && expiryDate != null
                && reissueDate.isAfter(expiryDate)) {
            errors.add("Reissue date must be before expiry date");
        }
    }

    private void throwIfErrors(List<String> errors) {
        if (!errors.isEmpty()) {
            throw new BusinessValidationException(
                    CardConstants.ERR_VALIDATION_FAILED,
                    "Validation failed: " + String.join("; ", errors),
                    errors);
        }
    }
}
