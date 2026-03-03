// domain/dto/AuthDto.java
package com.agilesolutions.card.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AuthDto {

    @Data @NoArgsConstructor @AllArgsConstructor
    @Schema(description = "Login request")
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        @Schema(example = "admin")
        private String username;

        @NotBlank(message = "Password is required")
        @Schema(example = "admin123")
        private String password;
    }

    @Data @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Schema(description = "Login response")
    public static class LoginResponse {
        private String accessToken;
        private String tokenType;
        private Long   expiresIn;
        private String username;
    }
}