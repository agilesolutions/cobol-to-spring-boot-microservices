// controller/AuthController.java
package com.agilesolutions.card.controller;

import com.agilesolutions.card.domain.dto.ApiResponseDto;
import com.agilesolutions.card.domain.dto.AuthDto;
import com.agilesolutions.card.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Auth controller replacing COBOL CICS SIGNON / SIGNOFF commands.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication",
     description = "JWT auth replacing COBOL CICS SIGNON/VERIFY PASSWORD")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider      jwtTokenProvider;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    // COBOL: EXEC CICS SIGNON USERID(...) PASSWORD(...)
    @PostMapping("/login")
    @Operation(summary     = "Authenticate user",
               description = "Replaces COBOL EXEC CICS SIGNON / VERIFY PASSWORD")
    public ResponseEntity<ApiResponseDto<AuthDto.LoginResponse>> login(
            @Valid @RequestBody AuthDto.LoginRequest loginRequest) {

        log.info("Login attempt: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(ApiResponseDto.success("Login successful",
                AuthDto.LoginResponse.builder()
                        .accessToken(token)
                        .tokenType("Bearer")
                        .expiresIn(jwtExpiration)
                        .username(authentication.getName())
                        .build()));
    }

    // COBOL: EXEC CICS SIGNOFF
    @PostMapping("/logout")
    @Operation(summary = "Logout",
               description = "Replaces COBOL EXEC CICS SIGNOFF")
    public ResponseEntity<ApiResponseDto<Void>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(
                ApiResponseDto.success("Logged out successfully", null));
    }
}