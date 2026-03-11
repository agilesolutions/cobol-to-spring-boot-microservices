package com.agilesolutions.card.rest;

import com.agilesolutions.card.domain.dto.ApiResponseDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

public class LegacyCardClientFallback implements LegacyFeignCardClient {

    @Override
    public ResponseEntity<ApiResponseDto<CardResponseDto>> getCard(@RequestHeader("service-correlation-id") String correlationId,
                                                                   @PathVariable String cardNum) {
        // Return a default response or an error response indicating the fallback
        ApiResponseDto<CardResponseDto> response = new ApiResponseDto<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Legacy service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
