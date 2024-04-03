package com.juank.microservices.confi.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import javax.swing.*;

@EnableConfigServer
@SpringBootApplication
public class ConfiServer {
    public static void main(String[] args) {
        SpringApplication.run(ConfiServer.class, args);
    }
}
