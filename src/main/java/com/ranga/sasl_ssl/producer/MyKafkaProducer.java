package com.ranga.sasl_ssl.producer;

import com.ranga.util.AppConfig;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.File;
import java.util.Properties;

public class MyKafkaProducer {

    public static void main(String[] args) {
        File file = new File("src/main/resources/log4j_sasl_ssl.properties");
        AppConfig appConfig = new AppConfig(PropertyUtil.getProperties(file));
        Properties kafkaProperties = getKafkaProperties(appConfig);
        String kafkaLoggerName = "kafkaSaslSslLogger";
        ProducerUtil producerUtil = new ProducerUtil(kafkaLoggerName, appConfig.getTopicName(), kafkaProperties);
        producerUtil.produce();
    }

    public static Properties getKafkaProperties(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, "kafka");

        Properties props = appConfig.getProperties();
        kafkaProperties.put(SaslConfigs.SASL_JAAS_CONFIG, props.getProperty("log4j.appender.KAFKA.clientJaasConfPath"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststoreLocation"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststorePassword"));

        return kafkaProperties;
    }
}