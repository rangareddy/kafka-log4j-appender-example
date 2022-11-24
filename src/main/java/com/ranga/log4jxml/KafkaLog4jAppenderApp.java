package com.ranga.log4jxml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ranga.util.AppConstants.KAFKA_LOGGER;

public class KafkaLog4jAppenderApp {

    private static final Logger kafkaLogger = LogManager.getLogger(KAFKA_LOGGER);
    private static final Logger logger = LogManager.getLogger(KafkaLog4jAppenderApp.class);

    public static void main(String[] args) {
        logger.info("Writing Log messages to Kafka");
        kafkaLogger.debug("Debug message from KafkaLog4jAppenderApp");
        kafkaLogger.info("Info message from KafkaLog4jAppenderApp");
        kafkaLogger.warn("Warn message from KafkaLog4jAppenderApp");
        logger.info("Messages are written to Kafka");
    }
}