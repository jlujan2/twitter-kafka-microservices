package com.juank.microservices.twitter_to_kafka.runner;

import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface StreamRunner {
    void start() throws TwitterException, IOException, URISyntaxException;
}
