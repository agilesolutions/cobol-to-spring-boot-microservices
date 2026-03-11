package com.agilesolutions.account.rest;

import com.agilesolutions.account.domain.dto.AccountResponseDto;
import com.agilesolutions.account.domain.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class LegacyAccountClientFallback implements LegacyFeignAccountClient {

    @Override
    public ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccount(String correlationId, String accountId) {
        // Return a default response or an error response indicating the fallback
        ApiResponseDto<AccountResponseDto> response = new ApiResponseDto<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Legacy service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
