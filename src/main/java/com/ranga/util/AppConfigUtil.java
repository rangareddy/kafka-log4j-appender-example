package com.ranga.util;

import java.util.Properties;

import static com.ranga.util.AppConstants.AUTO_OFFSET_RESET_CONFIG;
import static com.ranga.util.AppConstants.MY_CONSUMER_GROUP;

public class AppConfigUtil {

    private final Properties properties;
    private final String bootstrapServers;
    private final String topicName;

    public AppConfigUtil(Properties properties) {
        this.properties = properties;
        bootstrapServers = properties.getProperty("log4j.appender.KAFKA.brokerList");
        topicName = properties.getProperty("log4j.appender.KAFKA.topic");
    }

    public Properties getProperties() {
        return properties;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getConsumerGroupId() {
        return MY_CONSUMER_GROUP;
    }

    public String getAutoOffsetResetConfig() {
        return AUTO_OFFSET_RESET_CONFIG;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "bootstrapServers='" + getBootstrapServers() + '\'' +
                ", topicName='" + getTopicName() + '\'' +
                ", consumerGroupId='" + getConsumerGroupId() + '\'' +
                ", autoOffsetResetConfig='" + getAutoOffsetResetConfig() + '\'' +
                '}';
    }
}
