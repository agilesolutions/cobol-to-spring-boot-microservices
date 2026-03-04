// service/CardService.java
package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.dto.*;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface CardService {
    CardResponseDto createCard(CardRequestDto requestDto);
    CardResponseDto getCardByNum(String cardNum);
    CardResponseDto updateCard(String cardNum, CardUpdateDto updateDto);
    void            deactivateCard(String cardNum);
    PagedResponseDto<CardResponseDto> getAllCards(Pageable pageable);
    PagedResponseDto<CardResponseDto> getCardsByAccount(String acctId, Pageable pageable);
    PagedResponseDto<CardResponseDto> searchCards(
            String cardAcctId, String activeStatus,
            String embossedName, String groupId, Pageable pageable);
    PagedResponseDto<CardResponseDto> getExpiredCards(Pageable pageable);
    PagedResponseDto<CardResponseDto> getOverLimitCards(Pageable pageable);
    PagedResponseDto<CardResponseDto> getCardsExpiringBetween(
            LocalDate from, LocalDate to, Pageable pageable);
}