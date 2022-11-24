package com.ranga.sasl.consumer;

import com.ranga.util.AppConfigUtil;
import com.ranga.util.ConsumerUtil;
import com.ranga.util.PropertyUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import java.util.Properties;

import static com.ranga.util.AppConstants.SASL_KERBEROS_SERVICE_NAME;
import static org.apache.kafka.common.security.auth.SecurityProtocol.SASL_PLAINTEXT;

public class MyKafkaConsumer {

    private static final Logger logger = Logger.getLogger(MyKafkaConsumer.class.getName());

    public static Properties getKafkaProperties(AppConfigUtil appConfigUtil) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigUtil.getBootstrapServers());
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, appConfigUtil.getConsumerGroupId());
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfigUtil.getAutoOffsetResetConfig());
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SASL_PLAINTEXT.name);
        kafkaProperties.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, SASL_KERBEROS_SERVICE_NAME);
        return kafkaProperties;
    }

    public static void main(String[] args) {
        AppConfigUtil appConfigUtil = new AppConfigUtil(PropertyUtil.getProperties(args, "src/main/resources/log4j_sasl.properties"));
        ConsumerUtil consumerUtil = new ConsumerUtil(appConfigUtil.getTopicName(), getKafkaProperties(appConfigUtil));
        logger.info("Consuming messages from topic: " + appConfigUtil.getTopicName());
        consumerUtil.consume();
    }
}
