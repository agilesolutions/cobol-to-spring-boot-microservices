package com.agilesolutions.card.rest;

import com.agilesolutions.account.domain.dto.ApiResponseDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class LegacyCardClientFallback implements LegacyFeignAccountClient {

    @Override
    public ResponseEntity<ApiResponseDto<CardResponseDto>> getAccount(String correlationId, String accountId) {
        // Return a default response or an error response indicating the fallback
        ApiResponseDto<CardResponseDto> response = new ApiResponseDto<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Legacy service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
