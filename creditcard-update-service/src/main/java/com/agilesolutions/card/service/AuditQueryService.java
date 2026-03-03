// service/AuditQueryService.java
package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.dto.AuditLogResponseDto;
import com.agilesolutions.card.domain.dto.PagedResponseDto;
import com.agilesolutions.card.domain.entity.AuditLog;
import com.agilesolutions.card.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditQueryService {

    private final AuditLogRepository auditLogRepository;

    public PagedResponseDto<AuditLogResponseDto> getAuditLogByCard(
            String cardNum, Pageable pageable) {
        return toPagedResponse(
                auditLogRepository.findByEntityTypeAndEntityId(
                        "CARD", cardNum, pageable));
    }

    public PagedResponseDto<AuditLogResponseDto> getAuditLogByDateRange(
            LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return toPagedResponse(
                auditLogRepository.findByChangedAtBetween(from, to, pageable));
    }

    public PagedResponseDto<AuditLogResponseDto> getAuditLogByUser(
            String username, Pageable pageable) {
        return toPagedResponse(
                auditLogRepository.findByChangedBy(username, pageable));
    }

    private PagedResponseDto<AuditLogResponseDto> toPagedResponse(
            Page<AuditLog> page) {
        List<AuditLogResponseDto> content = page.getContent()
                .stream().map(this::toDto).toList();
        return PagedResponseDto.<AuditLogResponseDto>builder()
                .content(content).page(page.getNumber())
                .size(page.getSize()).totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages()).last(page.isLast())
                .build();
    }

    private AuditLogResponseDto toDto(AuditLog log) {
        return AuditLogResponseDto.builder()
                .id(log.getId()).entityType(log.getEntityType())
                .entityId(log.getEntityId()).action(log.getAction())
                .changedBy(log.getChangedBy()).changedAt(log.getChangedAt())
                .oldValue(log.getOldValue()).newValue(log.getNewValue())
                .ipAddress(log.getIpAddress())
                .build();
    }
}