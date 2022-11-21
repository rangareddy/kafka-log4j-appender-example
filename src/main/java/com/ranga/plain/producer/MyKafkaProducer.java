package com.ranga.producer;

import com.ranga.util.AppConfig;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class MyKafkaProducer {

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig(PropertyUtil.getProperties());
        Properties kafkaProperties = getKafkaProperties(appConfig);
        String kafkaLoggerName = "kafkaLogger";
        ProducerUtil producerUtil = new ProducerUtil(kafkaLoggerName, appConfig.getTopicName(), kafkaProperties);
        producerUtil.produce();
    }

    public static Properties getKafkaProperties(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return kafkaProperties;
    }
}