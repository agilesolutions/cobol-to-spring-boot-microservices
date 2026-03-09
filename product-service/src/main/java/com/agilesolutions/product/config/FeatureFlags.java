package com.agilesolutions.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "features")
@Data
public class FeatureFlags {

    private Map<String, Boolean> flags;
}
