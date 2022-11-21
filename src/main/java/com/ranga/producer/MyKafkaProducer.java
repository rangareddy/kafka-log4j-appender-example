package com.ranga.producer;

import com.ranga.util.AppConfig;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class MyKafkaProducer {

    private static final Logger logger = Logger.getLogger("kafkaLogger");
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void createTopicIfNotPresents(Properties kafkaProperties, String topicName) {
        Admin admin = Admin.create(kafkaProperties);
        try {
            if (!admin.listTopics().names().get().contains(topicName)) {
                admin.createTopics(Collections.singleton(new NewTopic(topicName, 1, (short) 1))).all().get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Properties properties = PropertyUtil.getProperties();
        AppConfig appConfig = new AppConfig(properties);
        Properties kafkaProperties = getKafkaProperties(appConfig);
        createTopicIfNotPresents(kafkaProperties, appConfig.getTopicName());
        Producer<String, String> producer = getProducer(appConfig);
        String message = df.format(new Date()) + " - Hello I am from "+MyKafkaProducer.class.getName();
        ProducerRecord<String, String> record = new ProducerRecord<>(appConfig.getTopicName(), message);
        ProducerCallBack callBack = new ProducerCallBack();
        producer.send(record, callBack);
        logger.info("Hello, I am from KafkaLog4jAppender");
        producer.close();
    }

    public static Producer<String, String> getProducer(AppConfig appConfig) {
        Properties kafkaProperties = getKafkaProperties(appConfig);
        return new KafkaProducer<>(kafkaProperties);
    }

    public static Properties getKafkaProperties(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return kafkaProperties;
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
}