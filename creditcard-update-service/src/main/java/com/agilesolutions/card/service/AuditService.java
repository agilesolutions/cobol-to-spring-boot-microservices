// service/AuditService.java
package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.entity.AuditLog;
import com.agilesolutions.card.domain.entity.Card;
import com.agilesolutions.card.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Audit service replacing COBOL VSAM audit-trail writes
 * after each successful REWRITE CARDDAT operation.
 *
 * COBOL equivalent:
 *   WRITE AUDITLOG FROM WS-AUDIT-RECORD
 *   after PROCESS-ENTER-KEY completion
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper       objectMapper;

    public void logCreate(String entityType, String entityId, Card card) {
        saveAuditLog(entityType, entityId, "CREATE", null, serializeCard(card));
    }

    public void logUpdate(String entityType, String entityId,
                          String oldValue, Card card) {
        saveAuditLog(entityType, entityId, "UPDATE", oldValue, serializeCard(card));
    }

    public void logDelete(String entityType, String entityId, String oldValue) {
        saveAuditLog(entityType, entityId, "DELETE", oldValue, null);
    }

    public String serializeCard(Card card) {
        try {
            return objectMapper.writeValueAsString(card);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize card for audit: {}", e.getMessage());
            return "{}";
        }
    }

    private void saveAuditLog(String entityType, String entityId,
                               String action, String oldValue, String newValue) {
        try {
            AuditLog entry = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action(action)
                    .changedBy(getCurrentUser())
                    .changedAt(LocalDateTime.now())
                    .oldValue(oldValue)
                    .newValue(newValue)
                    .build();
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.error("Failed to persist audit log: {}", e.getMessage(), e);
        }
    }

    private String getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated())
                ? auth.getName() : "ANONYMOUS";
    }
}