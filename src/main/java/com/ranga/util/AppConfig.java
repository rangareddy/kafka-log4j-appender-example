package com.ranga.util;

import java.util.Properties;

public class AppConfig {

    private final Properties properties;
    private final String bootstrapServers;
    private final String topicName;
    private final String consumerGroupId = "my_consumer_group";
    private final String autoOffsetResetConfig = "earliest";

    public AppConfig(Properties properties) {
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
        return consumerGroupId;
    }

    public String getAutoOffsetResetConfig() {
        return autoOffsetResetConfig;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", topicName='" + topicName + '\'' +
                ", consumerGroupId='" + consumerGroupId + '\'' +
                ", autoOffsetResetConfig='" + autoOffsetResetConfig + '\'' +
                '}';
    }
}
