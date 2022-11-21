package com.ranga.sasl_ssl.consumer;

import com.ranga.util.AppConfig;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyKafkaConsumer {

    private static final Logger logger = Logger.getLogger(MyKafkaConsumer.class.getName());

    public static Consumer<String, String> getConsumer(AppConfig appConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getConsumerGroupId());
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getAutoOffsetResetConfig());
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        Properties props = appConfig.getProperties();
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, props.getOrDefault("log4j.appender.KAFKA.securityProtocol", "SASL_SSL"));
        kafkaProperties.put(SaslConfigs.SASL_JAAS_CONFIG, props.getProperty("log4j.appender.KAFKA.clientJaasConfPath"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststoreLocation"));
        kafkaProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, props.getProperty("log4j.appender.KAFKA.sslTruststorePassword"));

        Consumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        consumer.subscribe(Collections.singletonList(appConfig.getTopicName()));
        return consumer;
    }

    public static void main(String[] args) {
        File file = new File("log4j_sasl_ssl.properties");
        Properties properties = PropertyUtil.getProperties(file);
        AppConfig appConfig = new AppConfig(properties);

        try (Consumer<String, String> consumer = getConsumer(appConfig)) {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
                logger.info("Total Records : " + records.count());
                records.forEach(record -> {
                    logger.info("Record Key " + record.key());
                    logger.info("Record value " + record.value());
                    logger.info("Record partition " + record.partition());
                    logger.info("Record offset " + record.offset());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
