//package com.juank.microservices.twitter_to_kafka.runner.impl;
//
//import com.juank.microservices.config_server.TwitterToKafkaServiceConfigData;
//import com.juank.microservices.twitter_to_kafka.runner.StreamRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
////@ConditionalOnProperty(name="twitter-to-kafka-service.enable-v2-tweets", havingValue = "true", matchIfMissing = true)
//@ConditionalOnExpression("${twitter-to-kafka-service.enable-v2-tweets} && not ${twitter-to-kafka-service.enable-mock-tweets}")
////@ConditionalOnProperty(name="twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
//public class TwitterV2KafkaStreamRunner implements StreamRunner {
//
//    public static final Logger LOG = LoggerFactory.getLogger(TwitterV2KafkaStreamRunner.class);
//
//    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
//
//    private final TwitterV2StreamHelper twitterV2StreamHelper;
//
//    public TwitterV2KafkaStreamRunner(TwitterToKafkaServiceConfigData configData, TwitterV2StreamHelper helper) {
//        twitterToKafkaServiceConfigData = configData;
//        twitterV2StreamHelper = helper;
//    }
//    @Override
//    public void start() {
//        String bearerToken = twitterToKafkaServiceConfigData.getTwitterV2BearerToken();
//        if (null != bearerToken) {
//            try {
//                twitterV2StreamHelper.setupRules(bearerToken, getRules());
//                twitterV2StreamHelper.connectStream(bearerToken);
//            } catch (IOException | URISyntaxException e) {
//                LOG.error("Error streaming tweets!", e);
//                throw new RuntimeException("Error streaming tweets!", e);
//            }
//        } else {
//            LOG.error("There was a problem getting your bearer token");
//            throw new RuntimeException("There was a problem with your bearer token");
//        }
//    }
//
//    private Map<String, String> getRules() {
//        List<String> keywords = twitterToKafkaServiceConfigData.getTwitterKeywords();
//        Map<String, String> rules = new HashMap<>();
//        for(String keyword: keywords) {
//            rules.put(keyword, "Keyword: "+keyword);
//        }
//        LOG.info("Created filter for twitter stream for keywords: {}", keywords);
//        return rules;
//    }
//}
