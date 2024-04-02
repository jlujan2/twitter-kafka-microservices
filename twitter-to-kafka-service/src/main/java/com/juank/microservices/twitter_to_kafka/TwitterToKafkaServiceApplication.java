package com.juank.microservices.twitter_to_kafka;

import com.juank.microservices.config_server.TwitterToKafkaServiceConfigData;
import com.juank.microservices.twitter_to_kafka.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import com.juank.microservices.twitter_to_kafka.runner.StreamRunner;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages="com.juank.microservices")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    @Autowired
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    private final StreamInitializer streamInitializer;


    private final StreamRunner streamRunner;
    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData configData,
                                            StreamRunner runner,
                                            StreamInitializer initializer
    ) {
        this.twitterToKafkaServiceConfigData = configData;
        this.streamRunner = runner;
        this.streamInitializer = initializer;
    }
    public static void main(String [] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        //LOG.info(Arrays.toString(twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[] {})));
        //LOG.info(twitterToKafkaServiceConfigData.getWelcomeMessage());
        streamInitializer.init();
        streamRunner.start();
    }
}
