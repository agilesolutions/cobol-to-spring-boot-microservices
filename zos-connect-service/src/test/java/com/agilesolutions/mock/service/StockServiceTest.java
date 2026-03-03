package com.agilesolutions.mock.service;

import com.agilesolutions.mock.config.TwelveDataProperties;
import com.agilesolutions.mock.domain.DailyStockData;
import com.agilesolutions.mock.domain.StockData;
import com.agilesolutions.mock.dto.StockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    private RestTemplate stockClient= mock(RestTemplate.class);

    @Mock
    private TwelveDataProperties twelveDataProperties;

    @InjectMocks
    private StockService stockService;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestStockPrices() {

        when(stockClient.getForObject(
                anyString(),
                eq(StockData.class),
                any(Object[].class))).thenReturn(mockStockData());

        StockDto data = stockService.getLatestStockPrices("AAPL");

        assert data.price().equals(1.0f);

    }

    private StockData mockStockData() {
        StockData stockData = StockData.builder().values(List.of(DailyStockData.builder().close("1").build())).build();
        return stockData;
    }

}