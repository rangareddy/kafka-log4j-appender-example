package com.ranga.util;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

public class ProducerUtil implements Closeable {

    private static final Logger logger = Logger.getLogger(ProducerUtil.class.getName());
    private final Producer<String, String> producer;
    private final String topicName;
    private final Logger kafkaLogger;

    public ProducerUtil(String kafkaLoggerName, String topicName, Properties kafkaProperties) {
        Objects.requireNonNull(topicName, "TopicName can't be null");
        Objects.requireNonNull(kafkaLoggerName, "KafkaLoggerName can't be null");
        this.topicName = topicName;
        this.producer = getProducer(kafkaProperties);
        this.kafkaLogger = Logger.getLogger(kafkaLoggerName);
        createTopicIfNotPresents(kafkaProperties, topicName);
    }

    public static void createTopicIfNotPresents(Properties kafkaProperties, String topicNames) {
        Admin admin = Admin.create(kafkaProperties);
        try {
            for (String topicName : topicNames.split(",")) {
                if (!admin.listTopics().names().get().contains(topicName)) {
                    admin.createTopics(Collections.singleton(new NewTopic(topicName, 1, (short) 1))).all().get();
                    logger.info("Topic " + topicName + " created successfully");
                } else {
                    logger.debug("Topic " + topicName + " already exists");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Producer<String, String> getProducer(Properties kafkaProperties) {
        return new KafkaProducer<>(kafkaProperties);
    }

    public void produce() {
        String message = "Hello I am from Java producers produce() message";
        produce(message);
    }

    public void produce(String message) {
        Objects.requireNonNull(message, "Message can't be null");
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        ProducerCallBack callBack = new ProducerCallBack();
        producer.send(record, callBack);
        kafkaLogger.info(message);
    }

    @Override
    public void close() {
        if (producer != null) {
            producer.close();
            logger.info("Producer closed successfully");
        }
    }

    private static class ProducerCallBack implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error("Error while producing message to topic :" + recordMetadata, e);
            } else {
                String message = String.format("Sent message to topic: %s partition: %s  offset: %s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                logger.info(message);
            }
        }
    }
}
