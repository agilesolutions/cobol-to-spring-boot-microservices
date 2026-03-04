// domain/dto/ApiResponseDto.java
package com.agilesolutions.card.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Unified response envelope.
 * Replaces COBOL WS-RETURN-MSG / WS-ERROR-MSG commarea fields.
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response envelope")
public class ApiResponseDto<T> {

    private boolean success;
    private String  message;
    private T       data;
    private List<String> errors;
    private String  errorCode;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponseDto<T> success(String message, T data) {
        return ApiResponseDto.<T>builder()
                .success(true).message(message).data(data)
                .timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponseDto<T> error(
            String message, String errorCode, List<String> errors) {
        return ApiResponseDto.<T>builder()
                .success(false).message(message)
                .errorCode(errorCode).errors(errors)
                .timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponseDto<T> error(String message, String errorCode) {
        return error(message, errorCode, null);
    }
}