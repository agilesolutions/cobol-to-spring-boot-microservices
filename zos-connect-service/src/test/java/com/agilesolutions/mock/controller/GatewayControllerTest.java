package com.agilesolutions.mock.controller;

import com.agilesolutions.mock.domain.Account;
import com.agilesolutions.mock.domain.Client;
import com.agilesolutions.mock.dto.StockDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;

@GraphQlTest(GatewayController.class)
class GatewayControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockitoBean
    private com.agilesolutions.mock.rest.ClientHttpClient clientHttpClient;

    @MockitoBean
    private com.agilesolutions.mock.rest.AccountHttpClient accountHttpClient;

    @MockitoBean
    private com.agilesolutions.mock.service.StockService stockService;

    @MockitoBean
    private io.micrometer.observation.ObservationRegistry observationRegistry;

    /**
     * Test the clients query when there are multiple clients returned by the client service.
     */
    @Test
    @DisplayName("Test clients query with multiple clients")
    void testClientsQuery() {

        given(clientHttpClient.getAllClients())
                .willReturn(Flux.just(
                        new Client(1L, "John", "A.", "Doe"),
                        new Client(2L, "Jane", "B.", "Smith")));

        String query = """
                query {
                    clients {
                        id
                        firstName
                        middleName
                        lastName
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("clients")
                .entityList(Client.class) // Replace Object.class with the actual Client class if available
                .hasSizeGreaterThan(0)
                .containsExactly(
                        new Client(1L, "John", "A.", "Doe"),
                        new Client(2L, "Jane", "B.", "Smith")
                );

    }

    /**
     * Test the clients query when there are no clients returned by the client service.
     */
    @Test
    @DisplayName("Test clients query with no clients")
    void testClientsQueryNoClients() {

        given(clientHttpClient.getAllClients())
                .willReturn(Flux.empty());

        String query = """
                query {
                    clients {
                        id
                        firstName
                        middleName
                        lastName
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("clients")
                .entityList(Client.class) // Replace Object.class with the actual Client class if available
                .hasSize(0);

    }

    /**
     * Test the clients query when the client service returns an error.
     */
    @Test
    @DisplayName("Test clients query with error from client service")
    void testClientsQueryError() {

        given(clientHttpClient.getAllClients())
                .willReturn(Flux.error(new RuntimeException("Service unavailable")));

        String query = """
                query {
                    clients {
                        id
                        firstName
                        middleName
                        lastName
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assert errors.size() == 1;
                    assert errors.get(0).getMessage().contains("INTERNAL_ERROR");
                });

    }

    /**
     * Test the clients query with accounts.
     */
    @Test
    @DisplayName("Test clients query with accounts")
    void testClientsQueryWithAccounts() {

        given(clientHttpClient.getAllClients())
                .willReturn(Flux.just(
                        new Client(1L, "John", "A.", "Doe"),
                        new Client(2L, "Jane", "B.", "Smith")));

        // Additional mocking for accounts and stock service can be added here
        given(accountHttpClient.getAllAccounts(List.of(1L, 2L)))
                .willReturn(Flux.just(
                        new com.agilesolutions.mock.domain.Account("ACC123", 1L, "Savings Account", "Retail", 950, 1000.0f, LocalDateTime.of(2025, 12, 31, 0, 0)),
                        new com.agilesolutions.mock.domain.Account("ACC456", 2L, "Checking Account", "Retail", 1900, 2000.0f, LocalDateTime.of(2024, 11, 30,0 ,0 ))
                ));

        given(stockService.getLatestStockPrices("ACC123"))
                .willReturn(new StockDto(1100.0f));
        given(stockService.getLatestStockPrices("ACC456"))
                .willReturn(new StockDto( 2100.0f));

        String query = """
                query {
                    clients {
                        id
                        firstName
                        middleName
                        lastName
                        accounts {
                            number
                            description
                            lineOfBusiness
                            maturityDate
                            amount
                            openingDayBalance
                        }
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("clients")
                .entityList(Client.class)
                .hasSize(2)
                .containsExactly(new Client(1L, "John", "A.", "Doe"),
                        new Client(2L, "Jane", "B.", "Smith"))
                .path("clients[0].accounts")
                .entityList(com.agilesolutions.mock.domain.Account.class)
                .hasSize(1)
                .containsExactly(new Account("ACC123", null, "Savings Account", "Retail", 950, 1100.0f, LocalDateTime.of(2025,12,31,0,0))
                );


    }



}