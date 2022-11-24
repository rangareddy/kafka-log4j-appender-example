package com.ranga.sasl.producer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static com.ranga.util.AppConstants.KAFKA_SASL_LOGGER;
import static com.ranga.util.AppConstants.SASL_KERBEROS_SERVICE_NAME;
import static org.apache.kafka.common.security.auth.SecurityProtocol.SASL_PLAINTEXT;

public class MyKafkaProducer {

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties(args, "src/main/resources/log4j_sasl.properties"));
        Properties kafkaProperties = getKafkaProperties(appConfigUtil);
        ProducerUtil producerUtil = new ProducerUtil(KAFKA_SASL_LOGGER, appConfigUtil.getTopicName(), kafkaProperties);
        producerUtil.produce();
    }

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SASL_PLAINTEXT.name);
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, SASL_KERBEROS_SERVICE_NAME);
        return kafkaProperties;
    }
}