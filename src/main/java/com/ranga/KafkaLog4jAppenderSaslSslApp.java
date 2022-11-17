package com.ranga;

import org.apache.log4j.Logger;

public class KafkaLog4jAppenderSaslSslApp {
    private static final Logger kafkaLogger = Logger.getLogger("kafkaLogger");
    private static final Logger logger = Logger.getLogger(KafkaLog4jAppenderSaslSslApp.class.getName());

    public static void main(String[] args) {
        logger.info("Writing Log messages to Kafka");
        kafkaLogger.debug("Debug message from KafkaLog4jAppenderApp");
        kafkaLogger.info("Info message from KafkaLog4jAppenderApp");
        kafkaLogger.warn("Warn message from KafkaLog4jAppenderApp");
        logger.info("Messages are written to Kafka");
    }
}
