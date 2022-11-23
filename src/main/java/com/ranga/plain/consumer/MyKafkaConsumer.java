package com.ranga.plain.consumer;

import com.ranga.util.AppConfig;
import com.ranga.util.ConsumerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.util.Properties;

public class MyKafkaConsumer {

    private static final Logger logger = Logger.getLogger(MyKafkaConsumer.class.getName());

    public static Properties getKafkaProperties(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getConsumerGroupId());
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getAutoOffsetResetConfig());
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return kafkaProperties;
    }

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig(PropertyUtil.getProperties());
        ConsumerUtil consumerUtil = new ConsumerUtil(appConfig.getTopicName(), getKafkaProperties(appConfig));
        logger.info("Consuming messages from topic: " + appConfig.getTopicName());
        consumerUtil.consume();
    }
}
