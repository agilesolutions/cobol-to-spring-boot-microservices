package com.agilesolutions.account.rest;

import com.agilesolutions.account.domain.dto.AccountResponseDto;
import com.agilesolutions.account.domain.dto.ApiResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/api/accounts", accept = MediaType.APPLICATION_JSON_VALUE)
public interface LegacyAccountClient {

    @GetExchange
    ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccount(@RequestHeader("service-correlation-id") String correlationId
            , @PathVariable String accountId);


}
