package com.agilesolutions.card.rest;

import com.agilesolutions.card.domain.dto.ApiResponseDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@FeignClient(name = "zos-connect-service", fallback = LegacyCardClientFallback.class)
public interface LegacyFeignCardClient {

    @GetExchange
    ResponseEntity<ApiResponseDto<CardResponseDto>> getCard(@PathVariable String cardNum);

}
