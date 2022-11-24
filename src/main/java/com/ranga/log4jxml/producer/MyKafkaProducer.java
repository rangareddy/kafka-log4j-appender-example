package com.ranga.log4jxml.producer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.AppConstants;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

import static com.ranga.util.AppConstants.KAFKA_LOGGER;
import static org.apache.kafka.common.security.auth.SecurityProtocol.PLAINTEXT;

public class MyKafkaProducer {

    private static final Logger logger = LogManager.getLogger(MyKafkaProducer.class);
    private static final Logger kafkaLogger = LogManager.getLogger(KAFKA_LOGGER);

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties(args));
        Properties kafkaProperties = getKafkaProperties(appConfigUtil);
        Producer producer = getProducer(kafkaProperties);
        logger.info("Producing messages to topic: " + appConfigUtil.getTopicName());

        String message = "Hello I am MyKafkaProducer - log4j2.xml";
        ProducerRecord<String, String> record = new ProducerRecord<>(appConfigUtil.getTopicName(), message);
        ProducerCallBack callBack = new ProducerCallBack();
        producer.send(record, callBack);
        kafkaLogger.info(message);
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

    public static Producer<String, String> getProducer(Properties kafkaProperties) {
        return new KafkaProducer<>(kafkaProperties);
    }

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, PLAINTEXT.name);
        return kafkaProperties;
    }
}