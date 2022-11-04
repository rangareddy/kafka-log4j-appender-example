package com.ranga.appender;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Properties;

public class MyKafkaLog4jAppender extends KafkaLog4jAppender {

    public MyKafkaLog4jAppender() {
        super();
    }

    @Override
    protected Producer<byte[], byte[]> getKafkaProducer(Properties props) {
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.25.37.70:9092");
        return new KafkaProducer<>(props);
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    public void append(LoggingEvent event) {
        try {
            if (super.getProducer() == null) {
                activateOptions();
            }
            super.append(event);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
