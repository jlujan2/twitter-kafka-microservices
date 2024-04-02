package com.juank.microservices.kafka.admin.config;

import com.juank.microservices.config_server.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry
@Configuration
public class KafkaAdminConfig {

    private final KafkaConfigData kafkaConfigData;

    public KafkaAdminConfig(KafkaConfigData configData) {
        this.kafkaConfigData = configData;
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                kafkaConfigData.getBootStrapServers()));
    }
}
