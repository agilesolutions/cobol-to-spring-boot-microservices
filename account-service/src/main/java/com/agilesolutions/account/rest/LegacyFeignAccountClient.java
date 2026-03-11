package com.agilesolutions.account.rest;

import com.agilesolutions.account.domain.dto.AccountResponseDto;
import com.agilesolutions.account.domain.dto.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

@FeignClient(name = "zos-connect-service", fallback = LegacyAccountClientFallback.class)
public interface LegacyFeignAccountClient {

    @GetExchange
    ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccount(@RequestHeader("service-correlation-id") String correlationId
            , @PathVariable String accountId);


}
