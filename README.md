# Kafka Log4j Appender Example

### [KafkaLog4jAppender](https://github.com/apache/kafka/blob/trunk/log4j-appender/src/main/java/org/apache/kafka/log4jappender/KafkaLog4jAppender.java)

KafkaLog4jAppender is a log4j appender that produces log messages to Kafka topic.

There are two ways we can configure the log4j properties:

1. log4j.properties file
2. log4j2.xml file

#### Sample log4j.properties

```properties
# Root logger option
log4j.rootLogger=INFO,console
# Redirect log messages to console
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.target=System.err
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d{yyyy/MM/dd HH:mm:ss} %-5p %c{1}:%L %m%n
# Redirect log messages to kafka
log4j.appender.KAFKA=org.apache.kafka.log4jappender.KafkaLog4jAppender
log4j.appender.KAFKA.brokerList=localhost:9092
log4j.appender.KAFKA.topic=kafka_log4j_topic
log4j.appender.KAFKA.syncSend=true
log4j.appender.KAFKA.ignoreExceptions=false
log4j.appender.KAFKA.securityProtocol=PLAINTEXT
log4j.appender.KAFKA.layout=org.apache.log4j.PatternLayout
log4j.appender.KAFKA.layout.ConversionPattern=%d{yyyy/MM/dd HH:mm:ss} %-5p %c{1}:%L %m%n
log4j.logger.kafkaLogger=INFO,KAFKA
```

In order to pass external log4j.properties configuration file, we need to use `-Dlog4j.configuration`

```sh
-Dlog4j.configuration=file:/tmp/log4j.properties
```

#### Sample log4j2.xml file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configuration>

<Configuration status="INFO" name="kafka-log4j-appender-app" packages="com.ranga">

    <!-- Logging Properties -->
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy/MM/dd HH:mm:ss} %-5p %c{1}:%L %m%n</Property>
    </Properties>

    <Appenders>

        <!-- Console appender configuration -->
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <!-- Kafka appender configuration -->
        <Kafka name="Kafka" topic="kafka_log4j_topic">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Property name="bootstrap.servers">localhost:9092</Property>
        </Kafka>

        <Async name="Async">
            <AppenderRef ref="console"/>
            <AppenderRef ref="Kafka"/>
        </Async>

    </Appenders>

    <Loggers>
        <!-- Root logger -->
        <Root level="INFO">
            <AppenderRef ref="console"/>
        </Root>

        <Logger name="kafkaLogger" additivity="false" level="INFO">
            <AppenderRef ref="Kafka"/>
            <AppenderRef ref="console"/>
        </Logger>

        <Logger name="org.apache.kafka" level="INFO"/> <!-- avoid recursive logging -->
    </Loggers>
</Configuration>
```

In order to pass external log4j2.xml configuration file, we need to use `-Dlog4j2.configurationFile`

```sh
-Dlog4j2.configurationFile=file:/tmp/log4j2.xml
```

## Kafka Log4j Appender Integration

### 1. Using PLAINTEXT protocol

**Step1:** Download the `kafka-log4j-appender-example` project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd kafka-log4j-appender-example/
```

**Step2:** According to your cluster, update the following properties in `log4j.properties`.

`vi config/log4j.properties`

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
```

**Step3:** Build the `kafka-log4j-appender-example` project

```sh
mvn clean package -DskipTests
```

**Step4:** Run the following code to test

```sh
java -Dlog4j.configuration=file:config/log4j.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.plain.KafkaLog4jAppenderApp
```

**Step5:** Verify the log messages are written to Kafka topic `kafka_log4j_topic`

```sh
java -Dlog4j.configuration=file:config/log4j.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.plain.consumer.MyKafkaConsumer
```

### 2. Using SASL_PLAINTEXT protocol 

**Step1:** Download the `kafka-log4j-appender-example` project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd kafka-log4j-appender-example/
```

**Step2:** Creating and Exporting the kafka client jaas file

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

> According to your cluster update the `keyTab` and `principal` values in above config file.

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

**Step3:** According to your cluster, update the following property values in `log4j_sasl.properties`.

`vi config/log4j_sasl.properties`

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
log4j.appender.KAFKA.clientJaasConfPath=/tmp/kafka_client_jaas.conf
log4j.appender.KAFKA.kerb5ConfPath=/tmp/krb5.conf
```

**Step4:** Build the `kafka-log4j-appender-example` project

```sh
mvn clean package -DskipTests
```

**Step5:** Run the following code to test

```sh
java 
 -Dlog4j.configuration=file:config/log4j_sasl.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.sasl.KafkaLog4jAppenderSaslApp
```

>  If application expects client jaas file pass the below parameters with updated paths.
> -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf \
> -Djava.security.krb5.conf=/tmp/krb5.conf 

**Step6:** Verify the log messages are written to Kafka topic `kafka_log4j_sasl_topic`

```sh
java 
 -Dlog4j.configuration=file:config/log4j_sasl.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.sasl.consumer.MyKafkaConsumer
```

### 2. Using SASL_SSL protocol

**Step1:** Download the `kafka-log4j-appender-example` project

```sh
git clone https://github.com/rangareddy/kafka-log4j-appender-example.git
cd kafka-log4j-appender-example/
```

**Step2:** Creating and Exporting the kafka client jaas file

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

> According to your cluster update the `keyTab` and `principal` values in above config file.

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
```

**Step3:** According to your cluster, update the following property values in `log4j_sasl_ssl.properties`.

`vi config/log4j_sasl_ssl.properties`

```sh
log4j.appender.KAFKA.brokerList=localhost:9092
log4j.appender.KAFKA.clientJaasConfPath=/tmp/kafka_client_jaas.conf
log4j.appender.KAFKA.kerb5ConfPath=/tmp/krb5.conf
log4j.appender.KAFKA.sslTruststoreLocation=</path/to/truststore>
log4j.appender.KAFKA.sslTruststorePassword=changeit
log4j.appender.KAFKA.sslKeystoreType=jks
log4j.appender.KAFKA.sslKeystoreLocation=</path/to/keystore>
log4j.appender.KAFKA.sslKeystorePassword=changeit
```

**Step4:** Build the `kafka-log4j-appender-example` project

```sh
mvn clean package -DskipTests
```

**Step5:** Run the following code to test

```sh
java 
 -Dlog4j.configuration=file:config/log4j_sasl_ssl.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.sasl_ssl.KafkaLog4jAppenderSaslSslApp
```

>  If application expects client jaas file pass the below parameters with updated paths.
> -Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf \
> -Djava.security.krb5.conf=/tmp/krb5.conf

**Step6:** Verify the log messages are written to Kafka topic `kafka_log4j_sasl_ssl_topic`

```sh
java 
 -Dlog4j.configuration=file:config/log4j_sasl_ssl.properties \
 -cp target/kafka-log4j-appender-example-1.0.0-SNAPSHOT.jar \
 com.ranga.sasl_ssl.consumer.MyKafkaConsumer
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
kafka-console-producer --broker-list `hostname -f`:9092 \
  --topic kafka_log4j_sasl_topic \
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
kafka-console-consumer --bootstrap-server `hostname -f`:9092 \
  --topic kafka_log4j_sasl_topic --from-beginning \
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