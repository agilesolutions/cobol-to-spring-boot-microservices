package com.agilesolutions.card.config;

import com.agilesolutions.card.rest.LegacyCardClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types = {LegacyCardClient.class})
@RequiredArgsConstructor
public class HttpServiceConfig {

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> {
            groups.forEachClient((group, builder) -> builder
                    .baseUrl("http://zos-connect-service")
                    .build());
        };
    }


}

