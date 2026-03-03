// controller/CardController.java
package com.agilesolutions.card.controller;

import com.agilesolutions.card.domain.dto.*;
import com.agilesolutions.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST Controller exposing COBOL COCRDUPC screen transactions as REST API.
 *
 * COBOL transaction COCRDUPC screen actions mapped to:
 *   CICS SEND MAP (display)      -> GET  endpoints
 *   CICS RECEIVE MAP (input)     -> POST/PUT/PATCH endpoints
 *   PF3 (Back)                   -> Stateless - not needed
 *   PF4 (Update/Confirm)         -> PUT  /cards/{cardNum}
 *   ENTER (Submit)               -> POST /cards
 *   PF5 (Deactivate)             -> DELETE /cards/{cardNum}
 */
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Card Management",
     description = "Card CRUD operations refactored from COBOL COCRDUPC.cbl")
@SecurityRequirement(name = "bearerAuth")
public class CardController {

    private final CardService cardService;

    // ─── COBOL: PROCESS-ENTER-KEY (CREATE) + WRITE CARDDAT ───────────────────
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Create card",
        description = "Replaces COBOL PROCESS-ENTER-KEY + WRITE CARDDAT paragraph"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Card created"),
        @ApiResponse(responseCode = "400", description = "Validation error (COBOL SEND-ERRMSG)"),
        @ApiResponse(responseCode = "409", description = "Card already exists (FILE STATUS '22')"),
        @ApiResponse(responseCode = "403", description = "Forbidden (CICS NOTAUTH)")
    })
    public ResponseEntity<ApiResponseDto<CardResponseDto>> createCard(
            @Valid @RequestBody CardRequestDto requestDto) {

        log.info("POST /cards - cardNum={}", requestDto.getCardNum());
        CardResponseDto response = cardService.createCard(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Card created successfully", response));
    }

    // ─── COBOL: GET-CARD-DATA / READ CARDDAT KEY = CARD-NUM ──────────────────
    @GetMapping("/{cardNum}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary     = "Get card by number",
        description = "Replaces COBOL GET-CARD-DATA / READ CARDDAT paragraph"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card found"),
        @ApiResponse(responseCode = "404", description = "Card not found (FILE STATUS '23')")
    })
    public ResponseEntity<ApiResponseDto<CardResponseDto>> getCard(
            @Parameter(description = "16-digit card number",
                       example = "4000200030004000")
            @PathVariable String cardNum) {

        log.debug("GET /cards/{}", cardNum);
        return ResponseEntity.ok(ApiResponseDto.success(
                "Card retrieved", cardService.getCardByNum(cardNum)));
    }

    // ─── COBOL: UPDATE-CARD-INFO + REWRITE CARDDAT ───────────────────────────
    @PutMapping("/{cardNum}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Full card update",
        description = "Replaces COBOL UPDATE-CARD-INFO + REWRITE CARDDAT paragraph"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card updated"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "409", description = "Concurrent update conflict")
    })
    public ResponseEntity<ApiResponseDto<CardResponseDto>> updateCard(
            @Parameter(description = "16-digit card number")
            @PathVariable String cardNum,
            @Valid @RequestBody CardUpdateDto updateDto) {

        log.info("PUT /cards/{}", cardNum);
        return ResponseEntity.ok(ApiResponseDto.success(
                "Card updated successfully",
                cardService.updateCard(cardNum, updateDto)));
    }

    // ─── COBOL: Modified screen fields only (PROCESS-ENTER-KEY partial) ───────
    @PatchMapping("/{cardNum}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Partial card update",
        description = "Replaces COBOL screen-field change detection in PROCESS-ENTER-KEY"
    )
    public ResponseEntity<ApiResponseDto<CardResponseDto>> patchCard(
            @PathVariable String cardNum,
            @RequestBody CardUpdateDto updateDto) {

        log.info("PATCH /cards/{}", cardNum);
        return ResponseEntity.ok(ApiResponseDto.success(
                "Card patched successfully",
                cardService.updateCard(cardNum, updateDto)));
    }

    // ─── COBOL: MOVE 'N' TO CARD-ACTIVE-STATUS + REWRITE CARDDAT ─────────────
    @DeleteMapping("/{cardNum}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Deactivate card",
        description = "Replaces COBOL logical delete: MOVE 'N' TO CARD-ACTIVE-STATUS + REWRITE"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Card deactivated"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    public ResponseEntity<Void> deactivateCard(
            @PathVariable String cardNum) {

        log.info("DELETE /cards/{}", cardNum);
        cardService.deactivateCard(cardNum);
        return ResponseEntity.noContent().build();
    }

    // ─── COBOL: START CARDDAT / READ NEXT (paginated browse) ─────────────────
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary     = "List all cards (paginated)",
        description = "Replaces COBOL START / READ NEXT browse on CARDDAT"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> getAllCards(
            @RequestParam(defaultValue = "0")        int    page,
            @RequestParam(defaultValue = "20")       int    size,
            @RequestParam(defaultValue = "cardNum")  String sortBy,
            @RequestParam(defaultValue = "ASC")      String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(ApiResponseDto.success("Cards retrieved",
                cardService.getAllCards(PageRequest.of(page, size, sort))));
    }

    // ─── COBOL: READ CARDDAT KEY = CARD-ACCT-ID ──────────────────────────────
    @GetMapping("/account/{acctId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary     = "Get cards by account ID",
        description = "Replaces COBOL READ CARDDAT KEY = CARD-ACCT-ID browse"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> getCardsByAccount(
            @Parameter(description = "11-digit account ID", example = "00001001001")
            @PathVariable String acctId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Cards for account retrieved",
                cardService.getCardsByAccount(acctId,
                        PageRequest.of(page, size, Sort.by("cardNum")))));
    }

    // ─── COBOL: Multi-key conditional READ NEXT ───────────────────────────────
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary     = "Search cards",
        description = "Multi-field search replacing COBOL keyed READ with qualifiers"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> searchCards(
            @Parameter(description = "Account ID filter")
            @RequestParam(required = false) String cardAcctId,
            @Parameter(description = "Status filter: ACTIVE or INACTIVE")
            @RequestParam(required = false) String activeStatus,
            @Parameter(description = "Embossed name partial match")
            @RequestParam(required = false) String embossedName,
            @Parameter(description = "Group ID filter")
            @RequestParam(required = false) String groupId,
            @RequestParam(defaultValue = "0")       int    page,
            @RequestParam(defaultValue = "20")      int    size,
            @RequestParam(defaultValue = "cardNum") String sortBy,
            @RequestParam(defaultValue = "ASC")     String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(ApiResponseDto.success("Search results",
                cardService.searchCards(cardAcctId, activeStatus, embossedName,
                        groupId, PageRequest.of(page, size, sort))));
    }

    // ─── COBOL: EVALUATE WHEN CARD-EXPIRAION-DATE < CURRENT-DATE ─────────────
    @GetMapping("/expired")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Get expired cards",
        description = "Replaces COBOL EVALUATE CARD-EXPIRAION-DATE < CURRENT-DATE branch"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> getExpiredCards(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Expired cards",
                cardService.getExpiredCards(
                        PageRequest.of(page, size, Sort.by("expiryDate")))));
    }

    // ─── COBOL: EVALUATE WHEN CARD-CURR-BAL > CARD-CREDIT-LIMIT ─────────────
    @GetMapping("/over-limit")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary     = "Get over-limit cards",
        description = "Replaces COBOL EVALUATE WHEN CARD-CURR-BAL > CARD-CREDIT-LIMIT"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> getOverLimitCards(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Over-limit cards",
                cardService.getOverLimitCards(
                        PageRequest.of(page, size, Sort.by("cardNum")))));
    }

    // ─── COBOL: EXPIRY-DATE BETWEEN WS-FROM AND WS-TO ────────────────────────
    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary     = "Get cards expiring in date range",
        description = "Replaces COBOL EVALUATE CARD-EXPIRAION-DATE BETWEEN dates"
    )
    public ResponseEntity<ApiResponseDto<PagedResponseDto<CardResponseDto>>> getCardsExpiring(
            @Parameter(description = "From date", example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date",   example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Cards expiring in range",
                cardService.getCardsExpiringBetween(from, to,
                        PageRequest.of(page, size, Sort.by("expiryDate")))));
    }
}