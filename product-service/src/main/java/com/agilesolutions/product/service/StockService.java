package com.agilesolutions.product.service;


import com.agilesolutions.product.config.ApplicationProperties;
import com.agilesolutions.product.domain.dto.StockDto;
import com.agilesolutions.product.domain.model.DailyStockData;
import com.agilesolutions.product.domain.model.StockData;
import com.agilesolutions.product.rest.StockClient;
import io.micrometer.core.annotation.Counted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class StockService {

    private final StockClient stockClient;

    private final ApplicationProperties applicationProperties;

    private static final String MINUTE_INTERVAL = "1min";
    private static final String DAY_INTERVAL = "1day";

    @PreAuthorize("hasRole('ADMIN')")
    @Counted(value = "stockPrices.service.invocations", description = "Number of times TwelveData.com the service is invoked")
    // Retry up to 3 times with a 2 second delay if IOException occurs
    @Retryable(includes = {IOException.class},
            maxRetries = 3L,
            delay = 200L,
            maxDelay = 2000L,
            multiplier = 2L)
    public StockDto getLatestStockPrices(@PathVariable String company) {

        log.info("Get stock prices for: {}", company);
        StockData data = stockClient.getLatestStockPrices(company, MINUTE_INTERVAL, 1, applicationProperties.getKey());
        DailyStockData latestData = data.getValues().get(0);
        log.info("Get stock prices ({}) -> {}", company, latestData.getClose());
        return new StockDto(Float.parseFloat(latestData.getClose()));

    }


}
