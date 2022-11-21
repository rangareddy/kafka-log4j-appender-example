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
mvn clean package -DskipTests
```

Step5: Run the following code to test

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.plain.KafkaLog4jAppenderApp
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

## SASL_PLAINTEXT

**Step1:** Exporting the kafka client jaas file

`vi /tmp/kafka_client_jaas.conf`

```shell
KafkaClient{
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    storeKey=true
    keyTab="/tmp/kafka.keytab"
    principal="kafka/localhost@HADOOP.COM"
    serviceName="kafka";
};
```

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

**Step2:** Create a Kafka topic

`vi /tmp/config.properties`

```shell
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

```shell
kafka-topics --create --bootstrap-server 172.25.40.135:9092 \
  --replication-factor 1 --partitions 3 --topic kafka_log4j_sasl_topic \
  --command-config /tmp/config.properties
```

**Step3:** Kafka Console Producer

`vi /tmp/producer.properties`

```shell
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

```shell
kafka-console-producer --broker-list 172.25.40.135:9092 \
  --topic kafka_log4j_sasl_topic \
  --producer.config /tmp/producer.properties
```

**Step4:** Kafka Console Consumer

`vi /tmp/consumer.properties`

```shell
security.protocol=SASL_PLAINTEXT
group.id=my_consumer_group
sasl.kerberos.service.name=kafka
```

```sh
kafka-console-consumer --bootstrap-server 172.25.40.135:9092 \
  --topic kafka_log4j_sasl_topic --from-beginning \
  --consumer.config /tmp/consumer.properties 
```

### Java API Example

**Step1:** Java Producer SASL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf \
 -Djava.security.krb5.conf=/tmp/krb5.conf \
 com.ranga.sasl.producer.MyKafkaProducer
```

**Step2:** Java Consumer SASL Example

```sh
java -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf -Djava.security.krb5.conf=/tmp/krb5.conf \
  -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl.consumer.MyKafkaConsumer
```

**Step3:** Java KafkaLog4jAppender SASL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl.KafkaLog4jAppenderSaslApp
```

## SASL_SSL

**Export the jaas file**

vi /tmp/kafka_client_jaas.conf

```shell
KafkaClient{
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    storeKey=true
    keyTab="/tmp/kafka.keytab"
    principal="kafka/localhost@HADOOP.COM"
    serviceName="kafka";
};
```

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

**Create a Kafka topic**

vi /tmp/config.properties

```shell
security.protocol=SASL_SSL 
ssl.truststore.location=/var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks
ssl.truststore.password=88NcTHAEfFzbneH5YzhoDfcQmVY23gPzkaaNdHhd4Mp
ssl.endpoint.identification.algorithm=
ssl.keystore.location=/var/run/cloudera-scm-agent/process/86-kafka-KAFKA_BROKER/cm-auto-host_keystore.jks
ssl.keystore.password=28jSzhtVA3BEBeQpC1PklOaGp8l80pm10X2bDLtb4qf
```

```shell
keytool -list -keystore /var/run/cloudera-scm-agent/process/86-kafka-KAFKA_BROKER/cm-auto-host_keystore.jks \
  -storepass 28jSzhtVA3BEBeQpC1PklOaGp8l80pm10X2bDLtb4qf 

keytool -v -keystore /var/run/cloudera-scm-agent/process/86-kafka-KAFKA_BROKER/cm-auto-host_keystore.jks -list

keytool -list -keystore /var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks \
  -storepass 88NcTHAEfFzbneH5YzhoDfcQmVY23gPzkaaNdHhd4Mp
```

```shell
kafka-topics --create --bootstrap-server 172.25.40.135:9092 \
  --replication-factor 1 --partitions 3 --topic kafka_log4j_sasl_ssl_topic --command-config /tmp/config.properties
```

**Kafka Console Producer**

`vi /tmp/producer.properties`

```shell
security.protocol=SASL_SSL 
ssl.truststore.location=/var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks
ssl.truststore.password=88NcTHAEfFzbneH5YzhoDfcQmVY23gPzkaaNdHhd4Mp
sasl.kerberos.service.name=kafka
```

```shell
kafka-console-producer --broker-list 172.25.40.135:9092 \
  --topic kafka_log4j_sasl_ssl_topic \
  --producer.config /tmp/producer.properties
```

**Kafka Console Consumer**

`vi /tmp/consumer.properties`

```shell
security.protocol=SASL_SSL
ssl.truststore.location=/var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks
ssl.truststore.password=88NcTHAEfFzbneH5YzhoDfcQmVY23gPzkaaNdHhd4Mp
group.id=my_consumer_group
```

```sh
kafka-console-consumer --bootstrap-server 172.25.40.135:9092 \
  --topic kafka_log4j_sasl_ssl_topic --from-beginning \
  --consumer.config /tmp/consumer.properties
```

**Java Producer SASL_SSL Example**

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.producer.MyKafkaProducer
```

**Java Consumer SASL_SSL Example**

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.consumer.MyKafkaConsumer
```

**Java KafkaLog4jAppender SASL_SSL Example**

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.KafkaLog4jAppenderSaslSslApp
```