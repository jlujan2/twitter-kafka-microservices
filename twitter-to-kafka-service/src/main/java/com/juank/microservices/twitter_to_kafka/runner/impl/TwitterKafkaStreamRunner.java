//package com.juank.microservices.twitter_to_kafka.runner.impl;
//
//import com.juank.microservices.config_server.TwitterToKafkaServiceConfigData;
//import com.juank.microservices.twitter_to_kafka.listener.TwitterKafkaStatusListener;
//import com.juank.microservices.twitter_to_kafka.runner.StreamRunner;
//import jakarta.annotation.PreDestroy;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.stereotype.Component;
//import twitter4j.FilterQuery;
//import twitter4j.TwitterException;
//import twitter4j.TwitterStream;
//import twitter4j.TwitterStreamFactory;
//
//import java.util.Arrays;
//
//@Component
////@ConditionalOnProperty(name="twitter-to-kafka-service.enable-v2-tweets", havingValue = "false")
//@ConditionalOnExpression("${twitter-to-kafka-service.enable-v2-tweets} && not ${twitter-to-kafka-service.enable-mock-tweets}")
////@ConditionalOnProperty(name="twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
//public class TwitterKafkaStreamRunner implements StreamRunner {
//
//    public static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);
//    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
//
//    //private final TwitterKafkaStatusListener twitterKafkaStatusListener;
//    private TwitterStream twitterStream;
//
//    public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
//                                    //TwitterKafkaStatusListener statusListener
//                                     ) {
//        //this.twitterKafkaStatusListener = statusListener;
//        this.twitterToKafkaServiceConfigData = configData;
//    }
//
//    @PreDestroy
//    public void shutDown() {
//        if(twitterStream!= null) {
//            LOG.info("Closing twitter stream!");
//            twitterStream.shutdown();
//        }
//    }
//    @Override
//    public void start() throws TwitterException {
//        //twitterStream = new TwitterStreamFactory().getInstance();
//        //twitterStream.addListener(twitterKafkaStatusListener);
//        addFilter();
//    }
//
//    private void addFilter() {
//        String [] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
//        FilterQuery filterQuery = new FilterQuery(keywords);
//        twitterStream.filter(filterQuery);
//        LOG.info("Started filter twitter stream for keywords {}", Arrays.toString(keywords));
//    }
//}
