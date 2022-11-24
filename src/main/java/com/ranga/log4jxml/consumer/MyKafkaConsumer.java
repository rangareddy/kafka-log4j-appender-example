package com.ranga.log4jxml.consumer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.ConsumerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.apache.kafka.common.security.auth.SecurityProtocol.PLAINTEXT;

public class MyKafkaConsumer {

    private static final Logger logger = LogManager.getLogger(MyKafkaConsumer.class);

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, appConfigUtil.getConsumerGroupId());
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfigUtil.getAutoOffsetResetConfig());
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, PLAINTEXT.name);
        return kafkaProperties;
    }

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties(args));
        Consumer<String, String> consumer = getOrCreateConsumer(appConfigUtil.getTopicName(), getKafkaProperties(appConfigUtil));
        logger.info("Consuming messages from topic: " + appConfigUtil.getTopicName());
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
            logger.info("Total Records : <" + records.count()+">");
            records.forEach(record -> {
                logger.info("Record partition: " + record.partition() + ", Record offset: " + record.offset() + ", Record Key: " + record.key() + ", Record value: " + record.value());
            });
        }
    }

    private static Consumer<String, String> getOrCreateConsumer(String topicNames, Properties kafkaProperties) {
        Objects.requireNonNull(topicNames, "TopicName(s) can't be null");
        Objects.requireNonNull(kafkaProperties, "KafkaProperties can't be null");
        Consumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        List<String> topics = new ArrayList<>();
        for (String topicName : topicNames.split(",")) {
            topics.add(topicName);
        }
        consumer.subscribe(topics);
        return consumer;
    }

}
