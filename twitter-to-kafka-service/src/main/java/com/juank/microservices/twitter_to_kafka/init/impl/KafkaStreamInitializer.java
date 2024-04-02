package com.juank.microservices.twitter_to_kafka.init.impl;

import com.juank.microservices.config_server.KafkaConfigData;
import com.juank.microservices.kafka.admin.client.KafkaAdminClients;
import com.juank.microservices.twitter_to_kafka.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements StreamInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamInitializer.class);

    private final KafkaConfigData kafkaConfigData;

    private final KafkaAdminClients kafkaAdminClients;


    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClients kafkaAdminClients) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClients = kafkaAdminClients;
    }

    @Override
    public void init() {
        kafkaAdminClients.createTopics();
        kafkaAdminClients.checkSchemaRegistry();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
