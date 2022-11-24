package com.ranga.util;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class ConsumerUtil implements Closeable {

    private static final Logger logger = Logger.getLogger(ConsumerUtil.class.getName());
    private final Consumer<String, String> consumer;

    public ConsumerUtil(String topicName, Properties kafkaProperties) {
        this.consumer = getOrCreateConsumer(topicName, kafkaProperties);
    }

    private Consumer<String, String> getOrCreateConsumer(String topicNames, Properties kafkaProperties) {
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

    public void consume() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
            logger.info("Total Records : <" + records.count()+">");
            records.forEach(record -> {
                logger.info("Record partition: " + record.partition() + ", Record offset: " + record.offset() + ", Record Key: " + record.key() + ", Record value: " + record.value());
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