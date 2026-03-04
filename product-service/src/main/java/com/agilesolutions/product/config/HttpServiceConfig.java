package com.agilesolutions.product.config;

import com.agilesolutions.product.rest.StockClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types = {StockClient.class})
@RequiredArgsConstructor
public class HttpServiceConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> {
            groups.forEachClient((group, builder) -> builder
                    .baseUrl(applicationProperties.getUrl())
                    .build());
        };
    }


}

