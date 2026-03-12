package com.agilesolutions.mock.controller;

import com.agilesolutions.mock.domain.dto.ApiResponseDto;
import com.agilesolutions.mock.domain.dto.CardResponseDto;
import com.agilesolutions.mock.domain.enums.CardStatus;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Credit Card Management",
        description = "Credit Card CRUD operations COBOL legacy")
@SecurityRequirement(name = "bearerAuth")
public class CreditCardController {

    // ─── COBOL: GET-ACCT-DATA (READ) ────────────────────────────────────────
    @GetMapping(value = "/{accountId}", version = "1.0.0")
    @Retryable(maxRetries = 3L)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary     = "Get card by number",
            description = "call COBOL GET-CARD-DATA / READ CARDDAT paragraph through gateway service (version 1.0.0 for backward compatibility with legacy clients)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card found"),
            @ApiResponse(responseCode = "404", description = "Card not found (COBOL NOT-FOUND path)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @RateLimiter(name= "getCard", fallbackMethod = "getCardFallback")
    public ResponseEntity<ApiResponseDto<CardResponseDto>> getCard(
            @RequestHeader("service-correlation-id") String correlationId,
            @Parameter(description = "16-digit card number",
                    example = "4000200030004000")
            @PathVariable String cardNum) {

        log.debug("GET /cards/{} - service-correlation-id {}", cardNum, correlationId);
        CardResponseDto response = CardResponseDto.builder()
                .id(1L)
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .cardCvvCd("123")
                .cardEmbossedName("ALICE JOHNSON")
                .activeStatus(CardStatus.Y)
                .currBal(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("3750.25"))
                .cashCreditLimit(new BigDecimal("2000.00"))
                .openDate(LocalDate.now().minusDays(30))
                .expiryDate(LocalDate.now().plusYears(3))
                .reissueDate(LocalDate.now().plusYears(2))
                .addrLine1("123 Main St")
                .addrState("NY")
                .addrCountry("USA")
                .addrZip("10001")
                .groupId("GRP001")
                .sli("001")
                .phoneNumber1("+12125551234")
                .build();

        return ResponseEntity.ok(ApiResponseDto.success("Credit Card retrieved", response));
    }

    public ResponseEntity<ApiResponseDto<CardResponseDto>> getCardFallback(
            @RequestHeader("service-correlation-id") String correlationId,
            @PathVariable String cardNum) {

        log.debug("GET /cards/{} RateLimitor fallback - service-correlation-id {}", cardNum, correlationId);
        CardResponseDto response = CardResponseDto.builder().build();

        return ResponseEntity.ok(ApiResponseDto.success("Credit Card fall back produced", response));
    }


}
