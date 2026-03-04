// mapper/CardMapper.java
package com.agilesolutions.card.mapper;

import com.agilesolutions.card.domain.dto.CardRequestDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import com.agilesolutions.card.domain.dto.CardUpdateDto;
import com.agilesolutions.card.domain.entity.Card;
import org.mapstruct.*;

/**
 * Maps between COBOL commarea field groups and Java DTOs.
 *
 * Mirrors COBOL MOVE statements:
 *   toEntity()            -> MOVE commarea fields TO WS-CARD-RECORD
 *   toResponseDto()       -> MOVE WS-CARD-RECORD  TO output commarea
 *   updateEntityFromDto() -> MOVE modified screen fields TO WS-CARD-RECORD
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy  = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy            = NullValueCheckStrategy.ALWAYS
)
public interface CardMapper {

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "currBal",      ignore = true)
    @Mapping(target = "currCycleCredit", ignore = true)
    @Mapping(target = "currCycleDebit",  ignore = true)
    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "updatedAt",    ignore = true)
    @Mapping(target = "version",      ignore = true)
    Card toEntity(CardRequestDto dto);

    @Mapping(target = "availableCredit",
             expression = "java(card.getAvailableCredit())")
    @Mapping(target = "expired",
             expression = "java(card.isExpired())")
    @Mapping(target = "overLimit",
             expression = "java(card.isOverLimit())")
    CardResponseDto toResponseDto(Card card);

    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "cardNum",         ignore = true)
    @Mapping(target = "cardAcctId",      ignore = true)
    @Mapping(target = "cardCvvCd",       ignore = true)
    @Mapping(target = "currBal",         ignore = true)
    @Mapping(target = "currCycleCredit", ignore = true)
    @Mapping(target = "currCycleDebit",  ignore = true)
    @Mapping(target = "openDate",        ignore = true)
    @Mapping(target = "createdBy",       ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updatedBy",       ignore = true)
    @Mapping(target = "updatedAt",       ignore = true)
    @Mapping(target = "version",         ignore = true)
    void updateEntityFromDto(CardUpdateDto dto, @MappingTarget Card card);
}