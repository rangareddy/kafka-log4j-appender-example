package com.ranga.consumer;

import com.ranga.util.AppConfig;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyKafkaConsumer {

    private static final Logger logger = Logger.getLogger(MyKafkaConsumer.class.getName());

    public static Consumer<String, String> getConsumer(AppConfig appConfig) {
        Properties kafkaParameters = new Properties();
        kafkaParameters.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaParameters.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getConsumerGroupId());
        kafkaParameters.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getAutoOffsetResetConfig());
        kafkaParameters.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParameters.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        Consumer<String, String> consumer = new KafkaConsumer<>(kafkaParameters);
        consumer.subscribe(Collections.singletonList(appConfig.getTopicName()));
        return consumer;
    }

    public static void main(String[] args) {
        Properties properties = PropertyUtil.getProperties();
        AppConfig appConfig = new AppConfig(properties);

        try (Consumer<String, String> consumer = getConsumer(appConfig)) {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
                logger.info("Total Records : " + records.count());
                records.forEach(record -> {
                    logger.info("Record Key " + record.key());
                    logger.info("Record value " + record.value());
                    logger.info("Record partition " + record.partition());
                    logger.info("Record offset " + record.offset());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
