package com.ranga.sasl_ssl.producer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.ProducerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

import static com.ranga.util.AppConstants.KAFKA_SASL_SSL_LOGGER;
import static com.ranga.util.AppConstants.SASL_KERBEROS_SERVICE_NAME;
import static org.apache.kafka.common.security.auth.SecurityProtocol.SASL_SSL;

public class MyKafkaProducer {

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties(args, "src/main/resources/log4j_sasl_ssl.properties"));
        Properties kafkaProperties = getKafkaProperties(appConfigUtil);
        ProducerUtil producerUtil = new ProducerUtil(KAFKA_SASL_SSL_LOGGER, appConfigUtil.getTopicName(), kafkaProperties);
        producerUtil.produce();
    }

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SASL_SSL);
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, SASL_KERBEROS_SERVICE_NAME);
        Properties props = appConfigUtil.getProperties();
        kafkaProperties.put(SaslConfigs.SASL_JAAS_CONFIG, props.getProperty("log4j.appender.KAFKA.clientJaasConfPath"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststoreLocation"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststorePassword"));
        return kafkaProperties;
    }
}