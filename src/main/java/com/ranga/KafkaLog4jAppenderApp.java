package com.ranga;

import org.apache.log4j.Logger;

public class KafkaLog4jAppenderApp {
    private static final Logger logger = Logger.getLogger(KafkaLog4jAppenderApp.class.getName());
    public static void main(String[] args) {
        System.out.println("Hello World!");
        logger.info("Hello World!");
    }
}
