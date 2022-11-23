package com.ranga.util;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

public class ConsumerUtil implements Closeable {

    private static final Logger logger = Logger.getLogger(ConsumerUtil.class.getName());
    private final Consumer<String, String> consumer;

    public ConsumerUtil(String topicName, Properties kafkaProperties) {
        this.consumer = getOrCreateConsumer(topicName, kafkaProperties);
    }

    private Consumer<String, String> getOrCreateConsumer(String topicName, Properties kafkaProperties) {
        Objects.requireNonNull(topicName, "TopicName can't be null");
        Objects.requireNonNull(kafkaProperties, "KafkaProperties can't be null");
        Consumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    public void consume() {
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
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.close();
        }
    }
}