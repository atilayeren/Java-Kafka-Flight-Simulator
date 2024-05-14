
In this project, WSL is used as a Kafka Server. Configuration is like below. <br>
3 different Java applications has been developed as clients to the WSL Kafka Server. <br>
4 different topics is used by clients to communicate:
<ul>
    <li>TargetPointPosition</li>
    <li>TowerPosition</li>
    <li>TargetBearingPosition</li>
    <li>CameraLosStatus</li>
</ul>

<br>  

# Kafka commands to use WSL as a kafka server

## run zookeeper
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
## run kafka
```bash
server.properties --> advertised.listeners=PLAINTEXT://"java_application_ip":9092
bin/kafka-server-start.sh config/server.properties
```

## create topic
```bash
/kafka_2.13-3.7.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic TargetPointPosition --partitions 1 --replication-factor 1 --config retention.ms=0
/kafka_2.13-3.7.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic TowerPosition --partitions 1 --replication-factor 1 --config retention.ms=0
/kafka_2.13-3.7.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic TargetBearingPosition --partitions 1 --replication-factor 1 --config retention.ms=0
/kafka_2.13-3.7.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic CameraLosStatus --partitions 1 --replication-factor 1 --config retention.ms=0
```

## delete topic
```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic TargetPointPosition
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic TowerPosition
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic TargetBearingPosition
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic CameraLosStatus
```

## create console consumer - use localhost
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic --from-beginning
```

## create console producer - use local host again
```bash
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic consumer_topic
```

## list topics
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

