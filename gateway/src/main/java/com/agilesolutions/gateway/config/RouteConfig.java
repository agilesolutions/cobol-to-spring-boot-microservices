package com.agilesolutions.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
                        .circuitBreaker(config -> config.setName("accountServiceCircuitBreaker")
                                .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://account-service"))
                .route("card-service", r -> r.path("/api/cards/**")
                        .filters(f -> f.addRequestHeader("API-Version", "2.0")
                                .addRequestHeader("X-Response-time", LocalDateTime.now().toString())
                        .circuitBreaker(config -> config.setName("cardServiceCircuitBreaker")
                                .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://card-service"))
                .route("legacy-account-service", r -> r.path("/legacy/api/accounts/**")
                        .filters(f -> f.stripPrefix(1)
                                .addRequestHeader("API-Version", "1.0")
                                .retry(config -> config.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000),2,true)))
                        .uri("lb://account-service"))
                .route("legacy-card-service", r -> r.path("/legacy/api/cards/**")
                        .filters(f -> f.stripPrefix(1).addRequestHeader("API-Version", "1.0")
                        .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                .setKeyResolver(userKeyResolver())))
                        .uri("lb://card-service"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }
}
