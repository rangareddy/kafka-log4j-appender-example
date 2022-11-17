# Kafka Log4j Appender Example

Step1: Create the Kafka topic `kafka_log4j_topic`

```sh
$ kafka-topics --create --bootstrap-server `hostname -f`:9092 --replication-factor 1 --partitions 3 --topic kafka_log4j_topic
$ kafka-topics --list --bootstrap-server `hostname -f`:9092
```

Step2: Download the `kafka-log4j-appender-example` project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd kafka-log4j-appender-example/
```

Step3: Update the bootstrapServers in `src/main/resources/log4j.properties`. For example,

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
```

> In log4j.properties, don't add KAFKA appender to rootLoger. It will throw the **org.apache.kafka.common.errors.TimeoutException: Topic kafka_log4j not present in metadata after 60000 ms** error.

Step4: Build the `kafka-log4j-appender-example` project

```sh
mvn clean package
```

Step5: Run the following code to test

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.KafkaLog4jAppenderApp
```

Step6: Verify the log messages are written to Kafka topic `kafka_log4j_topic`

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.consumer.MyKafkaConsumer
```

**Output**

```shell
2022/11/17 15:09:07 INFO  MyKafkaConsumer:40 Total Records : 2
2022/11/17 15:09:07 INFO  MyKafkaConsumer:42 Record Key null
2022/11/17 15:09:07 INFO  MyKafkaConsumer:43 Record value 2022-11-17 15:08:01 - Hello I am from com.ranga.producer.MyKafkaProducer
2022/11/17 15:09:07 INFO  MyKafkaConsumer:44 Record partition 0
2022/11/17 15:09:07 INFO  MyKafkaConsumer:45 Record offset 0
2022/11/17 15:09:07 INFO  MyKafkaConsumer:42 Record Key null
2022/11/17 15:09:07 INFO  MyKafkaConsumer:43 Record value 2022-11-17 15:08:46 - Hello I am from com.ranga.producer.MyKafkaProducer
2022/11/17 15:09:07 INFO  MyKafkaConsumer:44 Record partition 0
2022/11/17 15:09:07 INFO  MyKafkaConsumer:45 Record offset 1
```
