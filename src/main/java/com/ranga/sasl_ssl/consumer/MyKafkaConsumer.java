package com.ranga.sasl_ssl.consumer;

import com.ranga.util.AppConfig;
import com.ranga.util.ConsumerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Properties;

public class MyKafkaConsumer {

    private static final Logger logger = Logger.getLogger(MyKafkaConsumer.class.getName());

    public static Properties getKafkaProperties(AppConfig appConfig) {

        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getConsumerGroupId());
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getAutoOffsetResetConfig());
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, "kafka");

        Properties props = appConfig.getProperties();
        kafkaProperties.put(SaslConfigs.SASL_JAAS_CONFIG, props.getProperty("log4j.appender.KAFKA.clientJaasConfPath"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststoreLocation"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststorePassword"));
        return kafkaProperties;
    }

    public static void main(String[] args) {
        File file = new File("log4j_sasl_ssl.properties");
        AppConfig appConfig = new AppConfig(PropertyUtil.getProperties(file));
        ConsumerUtil consumerUtil = new ConsumerUtil(appConfig.getTopicName(), getKafkaProperties(appConfig));
        logger.info("Consuming messages from topic: " + appConfig.getTopicName());
        consumerUtil.consume();
    }
}
