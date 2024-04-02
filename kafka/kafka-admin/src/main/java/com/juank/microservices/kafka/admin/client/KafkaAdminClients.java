package com.juank.microservices.kafka.admin.client;

import com.juank.microservices.config_server.KafkaConfigData;
import com.juank.microservices.config_server.RetryConfigData;
import com.juank.microservices.kafka.admin.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClients {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaAdminClients.class);

    private final KafkaConfigData kafkaConfigData;

    private final RetryConfigData retryConfiData;

    private  final AdminClient adminClient;
    private final RetryTemplate retryTemplate;

    private final WebClient webClient;
    public KafkaAdminClients(KafkaConfigData kafkaConfigData, RetryConfigData retyrConfiData, AdminClient adminClient, RetryTemplate retryTemplate, WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfiData = retyrConfiData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
            LOG.info("Create topic result {}", createTopicsResult.values());
        }catch (Throwable t) {
            throw new KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t);
        }
        checkTopicsCreated();
    }

    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfiData.getMaxAttempts();
        int multiplier = retryConfiData.getMultiplier().intValue();
        Long sleeTimeMs = retryConfiData.getSleepTimeMs();
        for(String topic: kafkaConfigData.getTopicNamesToCreate()) {
            while(!isTopicCreated(topics, topic)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleeTimeMs);
                sleeTimeMs *= multiplier;
                topics = getTopics();
            }
        }
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics!");
        }
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = retryConfiData.getMaxAttempts();
        int multiplier = retryConfiData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfiData.getSleepTimeMs();
        while(!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatusCode getSchemaRegistryStatus() {
        try {
            return  webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return Mono.just(response.statusCode());
                        } else {
                            return Mono.just(HttpStatus.SERVICE_UNAVAILABLE);
                        }
                    }).block();
        }catch(Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if(retry > maxRetry)
            throw new KafkaClientException("Reached max number of retry for reading kafka topic(s)!");
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if(topics == null){
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        List<String> topicsNames = kafkaConfigData.getTopicNamesToCreate();
        LOG.info("Creating {} topic(s), attempt {}", topicsNames.size(), retryContext.getRetryCount());
        List<NewTopic>kafkaTopics = topicsNames.stream().map(topic -> new NewTopic(
                topic.trim(),
                kafkaConfigData.getNumOfPartitions(),
                kafkaConfigData.getReplicationFactor()
        )).collect(Collectors.toList());
        return adminClient.createTopics(kafkaTopics);
    }
    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch(Throwable t) {
            throw new KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        LOG.info("Reading kafka topic {}, attempt {}",
                kafkaConfigData.getTopicNamesToCreate().toArray(), retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (topics != null) {
            topics.forEach(topic -> LOG.debug("Topics with name {}", topic.name()));
        }
        return topics;
    }
}
