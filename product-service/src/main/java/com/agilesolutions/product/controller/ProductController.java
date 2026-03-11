package com.agilesolutions.product.controller;

import com.agilesolutions.product.domain.dto.StockDto;
import com.agilesolutions.product.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketTimeoutException;

@Tag(
        name = "CRUD REST APIs for accessing stock prices",
        description = "CRUD REST APIs to access stock prices"
)
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/assets")
public class ProductController {

    private final StockService stockService;

    @Operation(
            summary = "Get latest stock prices for a company",
            description = "REST API to get latest stock prices for a company"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/stockPrices/{company}", produces = MediaType.APPLICATION_JSON_VALUE,version = "1.0")
    // Jitter 50 : this ensures that even if 50 clients fail at the exact same moment, their retries are spread out over a wider window, smoothing the load on the target server.
    @Retryable(
            includes = {SocketTimeoutException.class, IOException.class}, // Only retry for these exceptions
            maxRetries = 4,      // Total attempts: 1 initial + 3 retries
            delay = 500,          // Initial delay (ms)
            multiplier = 2.0,     // Wait twice as long after each failure
            jitter = 50           // Randomly vary delay by +/- 50ms
    )public ResponseEntity<StockDto> getLatestStockPrices(@PathVariable("company") String company) {

        log.info("Get stock prices for: {}", company);

        StockDto stockResponse = stockService.getLatestStockPrices(company);

        return ResponseEntity.ok(stockResponse);
    }


}