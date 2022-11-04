package com.ranga;

import org.apache.log4j.Logger;

public class KafkaLog4jAppenderApp {
    private static final Logger logger = Logger.getLogger("kafkaLogger");
    public static void main(String[] args) {
        System.out.println("Writing Log messages to Kafka");
        logger.debug("Debug message from KafkaLog4jAppenderApp");
        logger.info("Info message from KafkaLog4jAppenderApp");
        logger.warn("Warn message from KafkaLog4jAppenderApp");
        System.out.println("Messages are written to Kafka");
    }
}
