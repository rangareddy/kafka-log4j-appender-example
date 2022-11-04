# Kafka Log4j Appender Example

Step1: Download the project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd /root/kafka-log4j-appender-example
```

Step2: Update the topicName and bootstrapServers in `src/main/resources/log4j.properties `

```sh
log4j.appender.KAFKA.brokerList=172.25.37.70:9092
log4j.appender.KAFKA.topic=kafka_log4j
```

> In log4j.properties, don't add KAFKA appender to rootLoger. It will throw the **org.apache.kafka.common.errors.TimeoutException: Topic kafka_log4j not present in metadata after 60000 ms** error.

Step3: Build the project

```sh
mvn clean package
```

Step4: Run the following code to test

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.KafkaLog4jAppenderApp
```

**Producer Example**

> Before running producer example, update the bootstrapServers value
> 
```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.producer.MyKafkaProducer
```

**Consumer Example** - 

> Before running consumer example, update the bootstrapServers value

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.consumer.MyKafkaConsumer
```