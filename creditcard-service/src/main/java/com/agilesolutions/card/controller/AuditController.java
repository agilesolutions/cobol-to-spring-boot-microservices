// controller/AuditController.java
package com.agilesolutions.card.controller;

import com.agilesolutions.card.domain.dto.*;
import com.agilesolutions.card.service.AuditQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Audit Log", description = "Card audit trail REST API")
@SecurityRequirement(name = "bearerAuth")
public class AuditController {

    private final AuditQueryService auditQueryService;

    @GetMapping("/card/{cardNum}")
    @Operation(summary     = "Get audit history for card",
               description = "Returns full audit trail for a card number")
    public ResponseEntity<ApiResponseDto<PagedResponseDto<AuditLogResponseDto>>>
    getCardAuditLog(
            @PathVariable String cardNum,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Audit log retrieved",
                auditQueryService.getAuditLogByCard(cardNum,
                        PageRequest.of(page, size,
                                Sort.by(Sort.Direction.DESC, "changedAt")))));
    }

    @GetMapping("/range")
    @Operation(summary = "Get audit log by date range")
    public ResponseEntity<ApiResponseDto<PagedResponseDto<AuditLogResponseDto>>>
    getAuditLogByRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Audit log retrieved",
                auditQueryService.getAuditLogByDateRange(from, to,
                        PageRequest.of(page, size,
                                Sort.by(Sort.Direction.DESC, "changedAt")))));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get audit log by user")
    public ResponseEntity<ApiResponseDto<PagedResponseDto<AuditLogResponseDto>>>
    getAuditLogByUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponseDto.success("Audit log retrieved",
                auditQueryService.getAuditLogByUser(username,
                        PageRequest.of(page, size,
                                Sort.by(Sort.Direction.DESC, "changedAt")))));
    }
}