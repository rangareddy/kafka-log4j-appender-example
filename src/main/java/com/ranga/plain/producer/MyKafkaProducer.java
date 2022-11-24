package com.ranga.plain.producer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;

import static org.apache.kafka.common.security.auth.SecurityProtocol.PLAINTEXT;

public class MyKafkaProducer {

    private static final Logger logger = Logger.getLogger(MyKafkaProducer.class.getName());

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties());
        Properties kafkaProperties = getKafkaProperties(appConfigUtil);
        String kafkaLoggerName = "kafkaLogger";
        ProducerUtil producerUtil = new ProducerUtil(kafkaLoggerName, appConfigUtil.getTopicName(), kafkaProperties);
        logger.info("Producing messages to topic: " + appConfigUtil.getTopicName());
        producerUtil.produce();
    }

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, PLAINTEXT);
        return kafkaProperties;
    }
}