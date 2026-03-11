package com.agilesolutions.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouteConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/api/assets/**")
                        .filters(f -> f.addRequestHeader("API-Version", "2.0")
                                .addRequestHeader("X-Response-time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://product-service"))
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.addRequestHeader("API-Version", "2.0")
                                .addRequestHeader("X-Response-time", LocalDateTime.now().toString())
                        .circuitBreaker(config -> config.setName("productServiceCircuitBreaker")
                                .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://account-service"))
                .route("card-service", r -> r.path("/api/cards/**")
                        .filters(f -> f.addRequestHeader("API-Version", "2.0")
                                .addRequestHeader("X-Response-time", LocalDateTime.now().toString())
                        .circuitBreaker(config -> config.setName("productServiceCircuitBreaker")
                                .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://card-service"))
                .route("legacy-account-service", r -> r.path("/legacy/api/accounts/**")
                        .filters(f -> f.stripPrefix(1).addRequestHeader("API-Version", "1.0"))
                        .uri("lb://account-service"))
                .route("legacy-card-service", r -> r.path("/legacy/api/cards/**")
                        .filters(f -> f.stripPrefix(1).addRequestHeader("API-Version", "1.0"))
                        .uri("lb://card-service"))
                .build();
    }
}
