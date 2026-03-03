// domain/dto/PagedResponseDto.java
package com.agilesolutions.card.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Paged response wrapper")
public class PagedResponseDto<T> {
    private List<T> content;
    private int     page;
    private int     size;
    private long    totalElements;
    private int     totalPages;
    private boolean last;
}