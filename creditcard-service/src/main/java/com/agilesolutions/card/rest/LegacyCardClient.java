package com.agilesolutions.card.rest;

import com.agilesolutions.card.domain.dto.ApiResponseDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/api/cards", accept = MediaType.APPLICATION_JSON_VALUE)
public interface LegacyCardClient {

    @GetExchange
    ResponseEntity<ApiResponseDto<CardResponseDto>> getCard(@PathVariable String cardNum);

}
