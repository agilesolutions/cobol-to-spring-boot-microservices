// domain/dto/CardUpdateDto.java
package com.agilesolutions.card.domain.dto;

import com.agilesolutions.card.domain.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Partial update DTO - maps COBOL PROCESS-ENTER-KEY modified screen fields.
 * Only non-null fields are applied (mirrors COBOL screen change detection).
 *
 * COBOL paragraph: EDIT-CARD-DATA (partial field edits)
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Card partial update payload")
public class CardUpdateDto {

    @Schema(description = "Name embossed on card")
    @Size(max = 50)
    private String cardEmbossedName;

    @Schema(description = "Card active status")
    private CardStatus activeStatus;

    @Schema(description = "Credit limit")
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit")
    @DecimalMin(value = "0.00", message = "Cash credit limit cannot be negative")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal cashCreditLimit;

    @Schema(description = "Card expiry date")
    private LocalDate expiryDate;

    @Schema(description = "Card reissue date")
    private LocalDate reissueDate;

    @Schema(description = "Group ID")
    @Size(max = 10)
    private String groupId;

    @Schema(description = "Service Level Indicator")
    @Size(max = 3)
    private String sli;

    @Schema(description = "ZIP code")
    @Size(max = 10)
    private String addrZip;

    @Schema(description = "State code (2 chars)")
    @Size(max = 2)
    private String addrState;

    @Schema(description = "Country code (3 chars)")
    @Size(max = 3)
    private String addrCountry;

    @Schema(description = "Address line 1")
    @Size(max = 50)
    private String addrLine1;

    @Schema(description = "Address line 2")
    @Size(max = 50)
    private String addrLine2;

    @Schema(description = "Primary phone number")
    @Pattern(regexp = "^[+0-9\\-() ]{0,15}$")
    private String phoneNumber1;

    @Schema(description = "Secondary phone number")
    @Pattern(regexp = "^[+0-9\\-() ]{0,15}$")
    private String phoneNumber2;
}