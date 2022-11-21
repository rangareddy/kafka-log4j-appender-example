package com.ranga.sasl.producer;

import com.ranga.util.AppConfig;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class MyKafkaProducer {
    private static final Logger kafkaSaslLogger = Logger.getLogger("kafkaSaslLogger");
    private static final Logger logger = Logger.getLogger(MyKafkaProducer.class.getName());
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void createTopic(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", appConfig.getBootstrapServers());
        Admin admin = Admin.create(kafkaProperties);
        try {
            if (!admin.listTopics().names().get().contains(appConfig.getTopicName())) {
                admin.createTopics(Collections.singleton(new NewTopic(appConfig.getTopicName(), 1, (short) 1))).all().get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        File file = new File("src/main/resources/log4j_sasl.properties");
        Properties properties = PropertyUtil.getProperties(file);
        AppConfig appConfig = new AppConfig(properties);
        createTopic(appConfig);
        Producer<String, String> producer = getProducer(appConfig);
        String message = df.format(new Date()) + " - Hello I am from "+ MyKafkaProducer.class.getName();
        logger.info("Sending message: "+message);
        ProducerRecord<String, String> record = new ProducerRecord<>(appConfig.getTopicName(), message);
        producer.send(record);
        ProducerCallBack callBack = new ProducerCallBack();
        producer.send(record, callBack);
        kafkaSaslLogger.info("Hello, I am from KafkaLog4jAppender");
        producer.close();
        logger.info("MyKafkaProducer finished");
    }

    public static Producer<String, String> getProducer(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, "kafka");
        return new KafkaProducer<>(kafkaProperties);
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
