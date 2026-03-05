// domain/dto/CardRequestDto.java
package com.agilesolutions.mock.domain.dto;

import com.agilesolutions.card.domain.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO mapping COBOL DFHCOMMAREA input fields for COCRDUPC
 *
 * COBOL paragraphs served:
 *   PROCESS-ENTER-KEY   -> create / update trigger
 *   EDIT-CARD-DATA      -> validated by @Valid + ValidationService
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Card create request payload")
public class CardRequestDto {

    // COBOL: CARD-NUM PIC X(16) - must be 16 numeric digits
    @Schema(description = "16-digit card number", example = "4000200030004000")
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 digits")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 numeric digits")
    private String cardNum;

    // COBOL: CARD-ACCT-ID PIC X(11)
    @Schema(description = "Associated account ID (11 digits)", example = "00001001001")
    @NotBlank(message = "Account ID is required")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    @Pattern(regexp = "^[0-9]{11}$", message = "Account ID must be 11 numeric digits")
    private String cardAcctId;

    // COBOL: CARD-CVV-CD PIC X(03)
    @Schema(description = "CVV code (3 digits)", example = "123")
    @Pattern(regexp = "^[0-9]{3}$", message = "CVV must be 3 numeric digits")
    private String cardCvvCd;

    // COBOL: CARD-EMBOSSED-NAME PIC X(50)
    @Schema(description = "Name embossed on card", example = "ALICE JOHNSON")
    @Size(max = 50, message = "Embossed name must not exceed 50 characters")
    private String cardEmbossedName;

    // COBOL: CARD-ACTIVE-STATUS PIC X(01)
    @Schema(description = "Card active status", example = "ACTIVE")
    @NotNull(message = "Card status is required")
    private CardStatus activeStatus;

    // COBOL: CARD-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Schema(description = "Credit limit", example = "5000.00")
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid credit limit format")
    private BigDecimal creditLimit;

    // COBOL: CARD-CASH-CREDIT-LIMIT PIC S9(10)V99 COMP-3
    @Schema(description = "Cash credit limit", example = "2000.00")
    @DecimalMin(value = "0.00", message = "Cash credit limit cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid cash credit limit format")
    private BigDecimal cashCreditLimit;

    // COBOL: CARD-CURR-BAL PIC S9(10)V99 COMP-3
    @Schema(description = "Current balance", example = "1500.00")
    @DecimalMin(value = "0.00", message = "Current balance cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid balance format")
    private BigDecimal currBal;

    // COBOL: CARD-OPEN-DATE PIC X(10)
    @Schema(description = "Card open date", example = "2024-01-15")
    private LocalDate openDate;

    // COBOL: CARD-EXPIRAION-DATE PIC X(10)
    @Schema(description = "Card expiry date", example = "2027-01-15")
    private LocalDate expiryDate;

    // COBOL: CARD-REISSUE-DATE PIC X(10)
    @Schema(description = "Card reissue date", example = "2026-01-15")
    private LocalDate reissueDate;

    // COBOL: CARD-GROUP-ID PIC X(10)
    @Schema(description = "Group ID", example = "GRP001")
    @Size(max = 10)
    private String groupId;

    // COBOL: CARD-SLI PIC X(03) Service Level Indicator
    @Schema(description = "Service Level Indicator", example = "001")
    @Size(max = 3)
    private String sli;

    // COBOL: CARD-ADDR-ZIP PIC X(10)
    @Schema(description = "ZIP code", example = "10001")
    @Size(max = 10)
    private String addrZip;

    // COBOL: CARD-ADDR-STATE PIC X(02)
    @Schema(description = "State code", example = "NY")
    @Size(max = 2)
    private String addrState;

    // COBOL: CARD-ADDR-COUNTRY PIC X(03)
    @Schema(description = "Country code", example = "USA")
    @Size(max = 3)
    private String addrCountry;

    // COBOL: CARD-ADDR-LINE-1 PIC X(50)
    @Schema(description = "Address line 1", example = "123 Main Street")
    @Size(max = 50)
    private String addrLine1;

    // COBOL: CARD-ADDR-LINE-2 PIC X(50)
    @Schema(description = "Address line 2", example = "Apt 4B")
    @Size(max = 50)
    private String addrLine2;

    // COBOL: CARD-PHONE-NUMBER-1 PIC X(15)
    @Schema(description = "Primary phone", example = "+12125551234")
    @Pattern(regexp = "^[+0-9\\-() ]{0,15}$", message = "Invalid phone number format")
    private String phoneNumber1;

    // COBOL: CARD-PHONE-NUMBER-2 PIC X(15)
    @Schema(description = "Secondary phone", example = "+12125555678")
    @Pattern(regexp = "^[+0-9\\-() ]{0,15}$", message = "Invalid phone number format")
    private String phoneNumber2;
}