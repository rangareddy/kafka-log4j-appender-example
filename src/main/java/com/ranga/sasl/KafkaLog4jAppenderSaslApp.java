package com.ranga.sasl;

import org.apache.log4j.Logger;

import static com.ranga.util.AppConstants.KAFKA_SASL_LOGGER;

public class KafkaLog4jAppenderSaslApp {

    private static final Logger kafkaSaslLogger = Logger.getLogger(KAFKA_SASL_LOGGER);
    private static final Logger logger = Logger.getLogger(KafkaLog4jAppenderSaslApp.class.getName());

    public static void main(String[] args) {
        logger.info("Writing Log messages to Kafka");
        kafkaSaslLogger.debug("Debug message from KafkaLog4jAppenderApp");
        kafkaSaslLogger.info("Info message from KafkaLog4jAppenderApp");
        kafkaSaslLogger.warn("Warn message from KafkaLog4jAppenderApp");
        logger.info("Messages are written to Kafka");
    }
}
