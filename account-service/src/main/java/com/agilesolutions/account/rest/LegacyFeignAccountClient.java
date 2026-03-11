package com.agilesolutions.account.rest;

import com.agilesolutions.account.domain.dto.AccountResponseDto;
import com.agilesolutions.account.domain.dto.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "zos-connect-service", fallback = LegacyAccountClientFallback.class)
public interface LegacyFeignAccountClient {

    @GetMapping(value = "/api/accounts",consumes = "application/json")
    ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccount(@RequestHeader("service-correlation-id") String correlationId
            , @PathVariable String accountId);


}
