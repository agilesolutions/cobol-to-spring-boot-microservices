package com.agilesolutions.account.config;

import com.agilesolutions.account.rest.LegacyAccountClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types = {LegacyAccountClient.class})
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

