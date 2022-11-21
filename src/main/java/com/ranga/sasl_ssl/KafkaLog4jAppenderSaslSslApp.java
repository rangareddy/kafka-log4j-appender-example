package com.ranga.sasl_ssl;

import org.apache.log4j.Logger;

public class KafkaLog4jAppenderSaslSslApp {
    private static final Logger kafkaSaslSslLogger = Logger.getLogger("kafkaSaslSslLogger");
    private static final Logger logger = Logger.getLogger(KafkaLog4jAppenderSaslSslApp.class.getName());

    public static void main(String[] args) {
        logger.info("Writing Log messages to Kafka");
        kafkaSaslSslLogger.debug("Debug message from KafkaLog4jAppenderApp");
        kafkaSaslSslLogger.info("Info message from KafkaLog4jAppenderApp");
        kafkaSaslSslLogger.warn("Warn message from KafkaLog4jAppenderApp");
        logger.info("Messages are written to Kafka");
    }
}
