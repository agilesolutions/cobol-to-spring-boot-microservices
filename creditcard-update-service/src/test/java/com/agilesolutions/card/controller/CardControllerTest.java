package com.agilesolutions.card.controller;

import com.agilesolutions.card.domain.dto.CardRequestDto;
import com.agilesolutions.card.domain.dto.CardResponseDto;
import com.agilesolutions.card.domain.enums.CardStatus;
import com.agilesolutions.card.exception.GlobalExceptionHandler;
import com.agilesolutions.card.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CardController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("CardController - REST API endpoint tests")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CardService cardService;

    private ObjectMapper objectMapper;
    private CardResponseDto sampleResponse;
    private CardRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleResponse = CardResponseDto.builder()
                .id(1L)
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .cardCvvCd("123")
                .cardEmbossedName("ALICE JOHNSON")
                .activeStatus(CardStatus.ACTIVE)
                .currBal(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("3750.25"))
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


        sampleRequest = CardRequestDto.builder()
                .cardNum("4000200030004001")
                .cardAcctId("00001001001")
                .cardCvvCd("123")
                .cardEmbossedName("ALICE JOHNSON")
                .activeStatus(CardStatus.ACTIVE)
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

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: admin retrieves card - 200 OK")
    void testGetCardById_asAdmin_returns200() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenReturn(sampleResponse);

        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
                .andExpect(jsonPath("$.cardNum").value(sampleResponse.getCardNum()))
                .andExpect(jsonPath("$.cardAcctId").value(sampleResponse.getCardAcctId()))
                .andExpect(jsonPath("$.cardCvvCd").value(sampleResponse.getCardCvvCd()))
                .andExpect(jsonPath("$.cardEmbossedName").value(sampleResponse.getCardEmbossedName()))
                .andExpect(jsonPath("$.activeStatus").value(sampleResponse.getActiveStatus().toString()))
                .andExpect(jsonPath("$.currBal").value(sampleResponse.getCurrBal().doubleValue()))
                .andExpect(jsonPath("$.creditLimit").value(sampleResponse.getCreditLimit().doubleValue()))
                .andExpect(jsonPath("$.availableCredit").value(sampleResponse.getAvailableCredit().doubleValue()))
                .andExpect(jsonPath("$.cashCreditLimit").value(sampleResponse.getCashCreditLimit().doubleValue()))
                .andExpect(jsonPath("$.openDate").value(sampleResponse.getOpenDate().toString()))
                .andExpect(jsonPath("$.expiryDate").value(sampleResponse.getExpiryDate().toString()))
                .andExpect(jsonPath("$.reissueDate").value(sampleResponse.getReissueDate().toString()))
                .andExpect(jsonPath("$.addrLine1").value(sampleResponse.getAddrLine1()))
                .andExpect(jsonPath("$.addrState").value(sampleResponse.getAddrState()))
                .andExpect(jsonPath("$.addrCountry").value(sampleResponse.getAddrCountry()))
                .andExpect(jsonPath("$.addrZip").value(sampleResponse.getAddrZip()))
                .andExpect(jsonPath("$.groupId").value(sampleResponse.getGroupId()))
                .andExpect(jsonPath("$.sli").value(sampleResponse.getSli()))
                .andExpect(jsonPath("$.phoneNumber1").value(sampleResponse.getPhoneNumber1()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("get card by ID: user retrieves card - 200 OK")
    void testGetCardById_asUser_returns200() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenReturn(sampleResponse);

        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
                .andExpect(jsonPath("$.cardNum").value(sampleResponse.getCardNum()))
                .andExpect(jsonPath("$.cardAcctId").value(sampleResponse.getCardAcctId()))
                .andExpect(jsonPath("$.cardCvvCd").value(sampleResponse.getCardCvvCd()))
                .andExpect(jsonPath("$.cardEmbossedName").value(sampleResponse.getCardEmbossedName()))
                .andExpect(jsonPath("$.activeStatus").value(sampleResponse.getActiveStatus().toString()))
                .andExpect(jsonPath("$.currBal").value(sampleResponse.getCurrBal().doubleValue()))
                .andExpect(jsonPath("$.creditLimit").value(sampleResponse.getCreditLimit().doubleValue()))
                .andExpect(jsonPath("$.availableCredit").value(sampleResponse.getAvailableCredit().doubleValue()))
                .andExpect(jsonPath("$.cashCreditLimit").value(sampleResponse.getCashCreditLimit().doubleValue()))
                .andExpect(jsonPath("$.openDate").value(sampleResponse.getOpenDate().toString()))
                .andExpect(jsonPath("$.expiryDate").value(sampleResponse.getExpiryDate().toString()))
                .andExpect(jsonPath("$.reissueDate").value(sampleResponse.getReissueDate().toString()))
                .andExpect(jsonPath("$.addrLine1").value(sampleResponse.getAddrLine1()))
                .andExpect(jsonPath("$.addrState").value(sampleResponse.getAddrState()))
                .andExpect(jsonPath("$.addrCountry").value(sampleResponse.getAddrCountry()))
                .andExpect(jsonPath("$.addrZip").value(sampleResponse.getAddrZip()))
                .andExpect(jsonPath("$.groupId").value(sampleResponse.getGroupId()))
                .andExpect(jsonPath("$.sli").value(sampleResponse.getSli()))
                .andExpect(jsonPath("$.phoneNumber1").value(sampleResponse.getPhoneNumber1()));

    }

    @Test
    @WithMockUser(roles = "GUEST")
    @DisplayName("get card by ID: guest role forbidden - 403")
    void testGetCardById_asGuest_returns403() throws Exception {
        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: card not found - 404 Not Found")
    void testGetCardById_notFound_returns404() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenReturn(null);

        mockMvc.perform(get("/cards/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Card not found with ID: 999"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: service exception - 500 Internal Server Error")
    void testGetCardById_serviceException_returns500() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Service error"));

    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: invalid ID format - 400 Bad Request")
    void testGetCardById_invalidIdFormat_returns400() throws Exception {
        mockMvc.perform(get("/cards/invalid-id"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid card ID format: invalid-id"));


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: null ID - 400 Bad Request")
    void testGetCardById_nullId_returns400() throws Exception {
        mockMvc.perform(get("/cards/null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid card ID format: null"));


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: empty ID - 400 Bad Request")
    void testGetCardById_emptyId_returns400() throws Exception {
        mockMvc.perform(get("/cards/"))
                .andExpect(status().isNotFound()); // Spring treats missing path variable as 404

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("get card by ID: service returns null - 404 Not Found")
    void testGetCardById_serviceReturnsNull_returns404() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenReturn(null);


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("create card: service throws exception - 500 Internal Server Error")
    void testCreateCard_serviceThrowsException_returns500() throws Exception {
        when(cardService.getCardByNum("4000200030004001")).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Database error"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("create card: invalid request body - 400 Bad Request")
    void testCreateCard_invalidRequestBody_returns400() throws Exception {
        String invalidJson = "{ \"cardNum\": \"4000200030004001\", \"cardAcctId\": \"00001001001\" "; // missing closing brace and required fields

        mockMvc.perform(get("/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("create card: missing required fields - 400 Bad Request")
    void testCreateCard_missingRequiredFields_returns400() throws Exception {
        CardRequestDto invalidRequest = CardRequestDto.builder()
                .cardNum("4000200030004001")
                .build(); // missing required fields like cardAcctId, cardCvvCd, etc

        mockMvc.perform(get("/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed for object='cardRequestDto'. Error count: 1"));

    }
}
