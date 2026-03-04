// service/CardServiceImpl.java
package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.dto.*;
import com.agilesolutions.card.domain.entity.Card;
import com.agilesolutions.card.domain.enums.CardStatus;
import com.agilesolutions.card.exception.*;
import com.agilesolutions.card.mapper.CardMapper;
import com.agilesolutions.card.repository.CardRepository;
import com.agilesolutions.card.util.CardConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Core business logic service.
 *
 * Refactored from COBOL COCRDUPC.cbl paragraphs:
 *
 *   MAIN-PARA             -> dispatch to create/update/get
 *   PROCESS-ENTER-KEY     -> createCard() / updateCard()
 *   GET-CARD-DATA         -> getCardByNum()
 *   EDIT-CARD-DATA        -> ValidationService.validateCardData()
 *   EDIT-CARDNUM          -> validateCardNum() in ValidationService
 *   EDIT-ACCTID           -> validateAcctId() in ValidationService
 *   VALIDATE-ACCOUNT-DATA -> validateAccountExists() in ValidationService
 *   UPDATE-CARD-INFO      -> updateCard() persistence phase
 *   SEND-ERRMSG           -> BusinessValidationException
 *   SEND-PLAIN-TEXT       -> ApiResponseDto.success()
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository    cardRepository;
    private final CardMapper        cardMapper;
    private final ValidationService validationService;
    private final AuditService      auditService;

    /**
     * COBOL: PROCESS-ENTER-KEY (CREATE) + WRITE CARDDAT
     */
    @Override
    public CardResponseDto createCard(CardRequestDto requestDto) {
        log.info("Creating card: {}", requestDto.getCardNum());

        // COBOL: EDIT-CARDNUM - duplicate check
        if (cardRepository.existsByCardNum(requestDto.getCardNum())) {
            throw new BusinessValidationException(
                    CardConstants.ERR_CARD_EXISTS,
                    "Card already exists: " + requestDto.getCardNum());
        }

        // COBOL: EDIT-CARD-DATA + VALIDATE-ACCOUNT-DATA
        validationService.validateCardData(requestDto);

        Card card = cardMapper.toEntity(requestDto);
        card.setOpenDate(requestDto.getOpenDate() != null
                ? requestDto.getOpenDate() : LocalDate.now());

        // COBOL: COMPUTE CURR-BAL default
        if (card.getCurrBal() == null)
            card.setCurrBal(requestDto.getCurrBal() != null
                    ? requestDto.getCurrBal() : java.math.BigDecimal.ZERO);

        Card saved = cardRepository.save(card);
        log.info("Card created: {}", saved.getCardNum());

        auditService.logCreate("CARD", saved.getCardNum(), saved);
        return cardMapper.toResponseDto(saved);
    }

    /**
     * COBOL: GET-CARD-DATA / READ CARDDAT KEY = CARD-NUM
     */
    @Override
    @Transactional(readOnly = true)
    public CardResponseDto getCardByNum(String cardNum) {
        log.debug("Fetching card: {}", cardNum);
        return cardMapper.toResponseDto(findCardOrThrow(cardNum));
    }

    /**
     * COBOL: PROCESS-ENTER-KEY (UPDATE) + REWRITE CARDDAT
     */
    @Override
    public CardResponseDto updateCard(String cardNum, CardUpdateDto updateDto) {
        log.info("Updating card: {}", cardNum);

        // COBOL: READ CARDDAT WITH LOCK
        Card card = findCardOrThrow(cardNum);

        // COBOL: IF CARD-ACTIVE-STATUS = 'N' PERFORM SEND-ERRMSG
        if (card.getActiveStatus() == CardStatus.Y) {
            throw new BusinessValidationException(
                    CardConstants.ERR_CARD_INACTIVE,
                    "Cannot update inactive card: " + cardNum);
        }

        // COBOL: IF CARD-EXPIRAION-DATE < CURRENT-DATE PERFORM SEND-ERRMSG
        if (card.isExpired()) {
            throw new BusinessValidationException(
                    CardConstants.ERR_CARD_EXPIRED,
                    "Cannot update expired card: " + cardNum);
        }

        // COBOL: EDIT-CARD-DATA (update variant)
        validationService.validateUpdateData(updateDto, card);

        String oldValue = auditService.serializeCard(card);

        // COBOL: MOVE modified fields TO WS-CARD-RECORD
        cardMapper.updateEntityFromDto(updateDto, card);

        Card updated = cardRepository.save(card);
        log.info("Card updated: {}", updated.getCardNum());

        auditService.logUpdate("CARD", updated.getCardNum(), oldValue, updated);
        return cardMapper.toResponseDto(updated);
    }

    /**
     * COBOL: MOVE 'N' TO CARD-ACTIVE-STATUS + REWRITE CARDDAT
     */
    @Override
    public void deactivateCard(String cardNum) {
        log.info("Deactivating card: {}", cardNum);
        Card card     = findCardOrThrow(cardNum);
        String oldVal = auditService.serializeCard(card);
        card.setActiveStatus(CardStatus.N);
        cardRepository.save(card);
        auditService.logDelete("CARD", cardNum, oldVal);
    }

    /**
     * COBOL: START CARDDAT / READ NEXT (paginated browse)
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> getAllCards(Pageable pageable) {
        return toPagedResponse(cardRepository.findAll(pageable));
    }

    /**
     * COBOL: READ CARDDAT KEY = CARD-ACCT-ID (all cards for account)
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> getCardsByAccount(
            String acctId, Pageable pageable) {
        return toPagedResponse(
                cardRepository.findByCardAcctId(acctId, pageable));
    }

    /**
     * COBOL: Conditional READ NEXT with key qualifiers
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> searchCards(
            String cardAcctId, String activeStatus,
            String embossedName, String groupId, Pageable pageable) {

        Page<Card> page;

        if (embossedName != null && !embossedName.isBlank()) {
            page = cardRepository.findByEmbossedNameContaining(embossedName, pageable);
        } else if (groupId != null && !groupId.isBlank()) {
            page = cardRepository.findByGroupId(groupId, pageable);
        } else if (cardAcctId != null && activeStatus != null) {
            page = cardRepository.findByCardAcctIdAndActiveStatus(
                    cardAcctId, CardStatus.fromCode(activeStatus), pageable);
        } else if (cardAcctId != null) {
            page = cardRepository.findByCardAcctId(cardAcctId, pageable);
        } else if (activeStatus != null) {
            page = cardRepository.findByActiveStatus(
                    CardStatus.fromCode(activeStatus), pageable);
        } else {
            page = cardRepository.findAll(pageable);
        }

        return toPagedResponse(page);
    }

    /**
     * COBOL: EVALUATE WHEN CARD-EXPIRAION-DATE < FUNCTION CURRENT-DATE
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> getExpiredCards(Pageable pageable) {
        return toPagedResponse(
                cardRepository.findExpiredCards(LocalDate.now(), pageable));
    }

    /**
     * COBOL: EVALUATE WHEN CARD-CURR-BAL > CARD-CREDIT-LIMIT
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> getOverLimitCards(Pageable pageable) {
        return toPagedResponse(cardRepository.findOverLimitCards(pageable));
    }

    /**
     * COBOL: EVALUATE WHEN CARD-EXPIRAION-DATE BETWEEN WS-FROM AND WS-TO
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CardResponseDto> getCardsExpiringBetween(
            LocalDate from, LocalDate to, Pageable pageable) {
        return toPagedResponse(
                cardRepository.findCardsExpiringBetween(from, to, pageable));
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Card findCardOrThrow(String cardNum) {
        return cardRepository.findByCardNum(cardNum)
                .orElseThrow(() -> new CardNotFoundException(
                        "Card not found: " + cardNum,
                        CardConstants.ERR_CARD_NOT_FOUND));
    }

    private PagedResponseDto<CardResponseDto> toPagedResponse(Page<Card> page) {
        List<CardResponseDto> content = page.getContent()
                .stream().map(cardMapper::toResponseDto).toList();
        return PagedResponseDto.<CardResponseDto>builder()
                .content(content).page(page.getNumber())
                .size(page.getSize()).totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages()).last(page.isLast())
                .build();
    }
}