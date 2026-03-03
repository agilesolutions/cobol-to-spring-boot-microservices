// domain/dto/AuditLogResponseDto.java
package com.agilesolutions.card.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Audit log entry")
public class AuditLogResponseDto {
    private Long          id;
    private String        entityType;
    private String        entityId;
    private String        action;
    private String        changedBy;
    private LocalDateTime changedAt;
    private String        oldValue;
    private String        newValue;
    private String        ipAddress;
}