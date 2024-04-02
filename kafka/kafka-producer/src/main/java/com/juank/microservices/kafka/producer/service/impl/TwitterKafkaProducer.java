package com.juank.microservices.kafka.producer.service.impl;

import com.juank.microservices.kafka.avro.model.TwitterAvroModel;
import com.juank.microservices.kafka.producer.service.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    public static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    private KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> template) {
        this.kafkaTemplate = template;
    }


    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        try {
            LOG.info("Sending message='{}' to topic='{}'", message, topicName);
            CompletableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture =
                    kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete(getCallback(topicName, message));
        } catch (Exception e) {
            LOG.error("Error sending message to kafka", e);
        }

    }


//    public void close() {
//        if(kafkaTemplate!= null) {
//            LOG.info("Closing Kafka producer!");
//            kafkaTemplate.destroy();
//        }
//    }

    private BiConsumer<SendResult<Long, TwitterAvroModel>, Throwable> getCallback(String topicName,
                                                                                  TwitterAvroModel message) {
        return (result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                LOG.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            } else {
                LOG.error("Error while sending message {} to topic {}", message.toString(), topicName, ex);
            }
        };
    }

}

