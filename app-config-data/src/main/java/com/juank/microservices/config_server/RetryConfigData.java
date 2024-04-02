package com.juank.microservices.config_server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {

    private Long initialIntervalMs;
    private Long maxIntervalMs;
    private Double multiplier;
    private Long sleepTimeMs;
    private Integer maxAttempts;
}
