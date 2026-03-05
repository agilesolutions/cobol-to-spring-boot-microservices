// domain/dto/CardResponseDto.java
package com.agilesolutions.mock.domain.dto;

import com.agilesolutions.mock.domain.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO - maps COBOL output commarea fields
 * Mirrors COBOL SEND MAP output paragraph for COCRDUPC
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Card response payload")
public class CardResponseDto {

    @Schema(description = "Surrogate key")
    private Long id;

    @Schema(description = "16-digit card number")
    private String cardNum;

    @Schema(description = "Associated account ID")
    private String cardAcctId;

    @Schema(description = "CVV code")
    private String cardCvvCd;

    @Schema(description = "Embossed name")
    private String cardEmbossedName;

    @Schema(description = "Card status")
    private CardStatus activeStatus;

    @Schema(description = "Current balance")
    private BigDecimal currBal;

    @Schema(description = "Credit limit")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Available credit (computed)")
    private BigDecimal availableCredit;

    @Schema(description = "Open date")
    private LocalDate openDate;

    @Schema(description = "Expiry date")
    private LocalDate expiryDate;

    @Schema(description = "Reissue date")
    private LocalDate reissueDate;

    @Schema(description = "Current cycle credit")
    private BigDecimal currCycleCredit;

    @Schema(description = "Current cycle debit")
    private BigDecimal currCycleDebit;

    @Schema(description = "Group ID")
    private String groupId;

    @Schema(description = "Service Level Indicator")
    private String sli;

    @Schema(description = "ZIP code")
    private String addrZip;

    @Schema(description = "State code")
    private String addrState;

    @Schema(description = "Country code")
    private String addrCountry;

    @Schema(description = "Address line 1")
    private String addrLine1;

    @Schema(description = "Address line 2")
    private String addrLine2;

    @Schema(description = "Primary phone")
    private String phoneNumber1;

    @Schema(description = "Secondary phone")
    private String phoneNumber2;

    @Schema(description = "Is card expired (derived)")
    private Boolean expired;

    @Schema(description = "Is card over limit (derived)")
    private Boolean overLimit;

    @Schema(description = "Created by")
    private String createdBy;

    @Schema(description = "Created at")
    private LocalDateTime createdAt;

    @Schema(description = "Updated by")
    private String updatedBy;

    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;

    @Schema(description = "Optimistic lock version")
    private Long version;
}