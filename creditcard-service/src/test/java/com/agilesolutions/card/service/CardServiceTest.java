package com.agilesolutions.card.service;

import com.agilesolutions.card.domain.dto.*;
import com.agilesolutions.card.domain.entity.Card;
import com.agilesolutions.card.domain.enums.CardStatus;
import com.agilesolutions.card.exception.*;
import com.agilesolutions.card.mapper.CardMapper;
import com.agilesolutions.card.repository.CardRepository;
import com.agilesolutions.card.service.CardServiceImpl;
import com.agilesolutions.card.util.CardConstants;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardService - COBOL COCRDUPC paragraph unit tests")
class CardServiceTest {

    @Mock private CardRepository    cardRepository;
    @Mock private CardMapper        cardMapper;
    @Mock private ValidationService validationService;
    @Mock private AuditService      auditService;

    @InjectMocks
    private CardServiceImpl cardService;

    private CardRequestDto  validRequest;
    private Card            sampleCard;
    private CardResponseDto sampleResponse;

    @BeforeEach
    void setUp() {
        validRequest = CardRequestDto.builder()
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .cardCvvCd("123")
                .cardEmbossedName("ALICE JOHNSON")
                .activeStatus(CardStatus.Y)
                .currBal(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .cashCreditLimit(new BigDecimal("2000.00"))
                .openDate(LocalDate.now().minusDays(30))
                .expiryDate(LocalDate.now().plusYears(3))
                .reissueDate(LocalDate.now().plusYears(2))
                .addrLine1("123 Main St")
                .addrState("NY")
                .addrCountry("USA")
                .addrZip("10001")
                .groupId("GRP001")
                .sli("001")
                .phoneNumber1("+12125551234")
                .build();

        sampleCard = Card.builder()
                .id(1L)
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .cardCvvCd("123")
                .cardEmbossedName("ALICE JOHNSON")
                .activeStatus(CardStatus.Y)
                .currBal(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .cashCreditLimit(new BigDecimal("2000.00"))
                .openDate(LocalDate.now().minusDays(30))
                .expiryDate(LocalDate.now().plusYears(3))
                .version(0L)
                .build();

        sampleResponse = CardResponseDto.builder()
                .id(1L)
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .activeStatus(CardStatus.Y)
                .currBal(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("3749.25"))
                .expired(false)
                .overLimit(false)
                .version(0L)
                .build();
    }

    // ─── CREATE tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("COBOL PROCESS-ENTER-KEY: successful card creation")
    void testCreateCard_success() {
        when(cardRepository.existsByCardNum("4000200030004001")).thenReturn(false);
        when(cardMapper.toEntity(any())).thenReturn(sampleCard);
        when(cardRepository.save(any())).thenReturn(sampleCard);
        when(cardMapper.toResponseDto(any())).thenReturn(sampleResponse);

        CardResponseDto result = cardService.createCard(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getCardNum()).isEqualTo("4000200030004001");
        assertThat(result.getAvailableCredit()).isEqualByComparingTo("3749.25");
        verify(cardRepository).existsByCardNum("4000200030004001");
        verify(validationService).validateCardData(validRequest);
        verify(cardRepository).save(any());
        verify(auditService).logCreate(eq("CARD"), eq("4000200030004001"), any());
    }

    @Test
    @DisplayName("COBOL EDIT-CARDNUM: duplicate card number rejected")
    void testCreateCard_duplicateCardNum_throwsException() {
        when(cardRepository.existsByCardNum("4000200030004001")).thenReturn(true);

        assertThatThrownBy(() -> cardService.createCard(validRequest))
                .isInstanceOf(BusinessValidationException.class)
                .satisfies(ex -> assertThat(
                        ((BusinessValidationException) ex).getErrorCode())
                        .isEqualTo(CardConstants.ERR_CARD_EXISTS));

        verify(cardRepository, never()).save(any());
        verify(validationService, never()).validateCardData(any());
    }

    // ─── GET tests ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("COBOL GET-CARD-DATA: successful card retrieval")
    void testGetCardByNum_success() {
        when(cardRepository.findByCardNum("4000200030004001"))
                .thenReturn(Optional.of(sampleCard));
        when(cardMapper.toResponseDto(sampleCard)).thenReturn(sampleResponse);

        CardResponseDto result = cardService.getCardByNum("4000200030004001");

        assertThat(result).isNotNull();
        assertThat(result.getCardNum()).isEqualTo("4000200030004001");
        verify(cardRepository).findByCardNum("4000200030004001");
    }

    @Test
    @DisplayName("COBOL FILE STATUS '23': card not found throws exception")
    void testGetCardByNum_notFound_throwsException() {
        when(cardRepository.findByCardNum("9999999999999999"))
                .thenReturn(Optional.empty());

        when(cardMapper.toResponseDto(any())).thenReturn(sampleResponse);

        assertThatThrownBy(() -> cardService.getCardByNum("9999999999999999"))
                .isInstanceOf(CardNotFoundException.class)
                .satisfies(ex -> assertThat(
                        ((CardNotFoundException) ex).getErrorCode())
                        .isEqualTo(CardConstants.ERR_CARD_NOT_FOUND));

    }
}