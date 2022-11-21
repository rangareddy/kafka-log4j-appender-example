package com.ranga.util;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class ProducerUtil implements Closeable {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Producer<String, String> producer;
    private final String topicName;
    private static final Logger logger = Logger.getLogger(ProducerUtil.class.getName());
    private final Logger kafkaLogger;

    public ProducerUtil(String kafkaLoggerName, String topicName, Properties kafkaProperties){
        this.topicName = topicName;
        this.producer = getProducer(kafkaProperties);
        this.kafkaLogger = Logger.getLogger(kafkaLoggerName);
        createTopicIfNotPresents(kafkaProperties, topicName);
    }

    public static void createTopicIfNotPresents(Properties kafkaProperties, String topicName) {
        Admin admin = Admin.create(kafkaProperties);
        try {
            if (!admin.listTopics().names().get().contains(topicName)) {
                admin.createTopics(Collections.singleton(new NewTopic(topicName, 1, (short) 1))).all().get();
                logger.info("Topic "+topicName+" created successfully");
            } else {
                logger.warn("Topic "+topicName+" already exists");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void produce() {
        String message = df.format(new Date()) + " - Hello I am from Java producer produce()";
        produce(message);
    }

    public void produce(String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        ProducerCallBack callBack = new ProducerCallBack();
        producer.send(record, callBack);
        kafkaLogger.info(message);
    }

    @Override
    public void close() throws IOException {
        if(producer != null) {
            producer.close();
        }
    }

    private static class ProducerCallBack implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error("Error while producing message to topic :" + recordMetadata, e);
            } else {
                String message = String.format("sent message to topic:%s partition:%s  offset:%s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                logger.info(message);
            }
        }
    }

    public static Producer<String, String> getProducer(Properties kafkaProperties) {
        return new KafkaProducer<>(kafkaProperties);
    }
}
