package com.agilesolutions.mock.controller;

import com.agilesolutions.mock.domain.dto.AccountResponseDto;
import com.agilesolutions.mock.domain.dto.ApiResponseDto;
import com.agilesolutions.mock.domain.enums.AccountStatus;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management",
        description = "Account CRUD operations COBOL legacy")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    // ─── COBOL: GET-ACCT-DATA (READ) ────────────────────────────────────────
    @GetMapping(value = "/{accountId}", version = "1.0.0")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary     = "Get account by ID",
            description = "COBOL GET-ACCT-DATA / READ ACCTDAT paragraph"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found (COBOL NOT-FOUND path)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @RateLimiter(name= "getAccount", fallbackMethod = "getAccountFallback")
    public ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccount(
            @RequestHeader("service-correlation-id") String correlationId,
            @Parameter(description = "11-digit account ID", example = "00001001001")
            @PathVariable String accountId) {

        log.debug("GET /accounts/{} - service-correlation-id {}", accountId, correlationId);
        AccountResponseDto response = AccountResponseDto.builder()
                .id(1L)
                .accountId("00001001001")
                .accountName("TEST ACCOUNT")
                .accountType("1")
                .accountTypeDescription("Credit")
                .activeStatus(AccountStatus.Y)
                .currBal(new BigDecimal("1500.00"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("3500.00"))
                .openDate(LocalDate.now().minusDays(30))
                .version(0L)
                .build();

        return ResponseEntity.ok(ApiResponseDto.success("Account retrieved", response));
    }

    public ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccountFallback(
            @RequestHeader("service-correlation-id") String correlationId,
            @PathVariable String accountId) {

        log.debug("GET /accounts/{} RateLimitor fallback - service-correlation-id {}", accountId, correlationId);
        AccountResponseDto response = AccountResponseDto.builder().build();

        return ResponseEntity.ok(ApiResponseDto.success("Account fall back produced", response));
    }


}
