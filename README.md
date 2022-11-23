# Kafka Log4j Appender Example

Download the `kafka-log4j-appender-example` project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd kafka-log4j-appender-example/
```

### PLAINTEXT

**Step1:** Update the bootstrapServers in `src/main/resources/log4j.properties`. For example,

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
```

**Step2:** Build the `kafka-log4j-appender-example` project

```sh
mvn clean package -DskipTests
```

**Step3:** Run the following code to test

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.plain.KafkaLog4jAppenderApp
```

**Step4:** Verify the log messages are written to Kafka topic `kafka_log4j_topic`

```sh
java -jar target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.plain.consumer.MyKafkaConsumer
```

### SASL_PLAINTEXT

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

**Step1:** Update the bootstrapServers in `src/main/resources/log4j_sasl.properties`. For example,

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
```

**Step2:** Java Producer SASL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf \
 -Djava.security.krb5.conf=/tmp/krb5.conf \
 com.ranga.sasl.producer.MyKafkaProducer
```

**Step3:** Java Consumer SASL Example

```sh
java -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf -Djava.security.krb5.conf=/tmp/krb5.conf \
  -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl.consumer.MyKafkaConsumer
```

**Step4:** Java KafkaLog4jAppender SASL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl.KafkaLog4jAppenderSaslApp
```

### SASL_SSL

**Step1:** Java Producer SASL_SSL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.producer.MyKafkaProducer
```

**Step2:** Java Consumer SASL_SSL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.consumer.MyKafkaConsumer
```

**Step3:** Java KafkaLog4jAppender SASL_SSL Example

```sh
java -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar com.ranga.sasl_ssl.KafkaLog4jAppenderSaslSslApp
```

## Kafka CLI Commands

### PLAINTEXT

#### Step1: Create the Kafka topic `kafka_log4j_topic`

```sh
kafka-topics --create --bootstrap-server localhost:9092 \
  --replication-factor 1 --partitions 3 --topic kafka_log4j_topic
```

Verify the topics

```sh
kafka-topics --list --bootstrap-server localhost:9092
```

#### Step3: Producing the Kafka Messages

```shell
kafka-console-producer --broker-list localhost:9092 \
  --topic kafka_log4j_topic
```

#### Step3: Consuming the Kafka Messages

```sh
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic kafka_log4j_topic --from-beginning 
```

### SASL_PLAINTEXT

#### Step1: Export the kafka client jaas file

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

```sh
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

#### Step2: Create the Kafka topic `kafka_log4j_topic`

`vi /tmp/config.properties`

```sh
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

```sh
kafka-topics --create --bootstrap-server localhost:9092 \
  --replication-factor 1 --partitions 3 --topic kafka_log4j_topic \
  --command-config /tmp/config.properties
```

Verify the topics

```sh
kafka-topics --list --bootstrap-server localhost:9092
```

#### Step3: Producing the Kafka Messages

`vi /tmp/producer.properties`

```shell
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

```shell
kafka-console-producer --broker-list localhost:9092 \
  --topic kafka_log4j_topic \
  --producer.config /tmp/producer.properties
```

#### Step4: Consuming the Kafka Messages

`vi /tmp/consumer.properties`

```sh
security.protocol=SASL_PLAINTEXT
group.id=my_consumer_group
sasl.kerberos.service.name=kafka
```

```sh
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic kafka_log4j_topic --from-beginning \
  --consumer.config /tmp/consumer.properties 
```

### SASL_SSL

#### Step1: Export the kafka client jaas file

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

```sh
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

#### Step2: Create the Kafka topic `kafka_log4j_topic`

`vi /tmp/config.properties`

```sh
security.protocol=SASL_SSL 
ssl.truststore.location=/tmp/my_truststore.jks
ssl.truststore.password=12345
ssl.keystore.location=/tmp/my_keystore.jks
ssl.keystore.password=12345
```

> keytool -list -keystore /tmp/my_keystore.jks -storepass 12345

```sh
kafka-topics --create --bootstrap-server `hostname -f`:9092 \
  --replication-factor 1 --partitions 3 --topic kafka_log4j_topic \
  --command-config /tmp/config.properties
```

Verify the topics

```sh
kafka-topics --list --bootstrap-server `hostname -f`:9092
```

#### Step3: Producing the Kafka Messages

`vi /tmp/producer.properties`

```sh
security.protocol=SASL_SSL 
ssl.truststore.location=/tmp/my_truststore.jks
ssl.truststore.password=12345
sasl.kerberos.service.name=kafka
```

```sh
kafka-console-producer --broker-list `hostname -f`:9092 \
  --topic kafka_log4j_topic \
  --producer.config /tmp/producer.properties
```

#### Step4: Consuming the Kafka Messages

`vi /tmp/consumer.properties`

```sh
security.protocol=SASL_SSL
ssl.truststore.location=/tmp/my_truststore.jks
ssl.truststore.password=12345
group.id=my_consumer_group
```

```sh
kafka-console-consumer --bootstrap-server `hostname -f`:9092 \
  --topic kafka_log4j_topic --from-beginning \
  --consumer.config /tmp/consumer.properties
```