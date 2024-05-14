package world.kafka;

public abstract class AbstractKafkaClient implements Runnable {
    protected static final String BrokerIPAddress = "172.23.72.89:9092";
    protected final String topicName;

    public AbstractKafkaClient(String topicName) {
        this.topicName = topicName;
        this.configureKafka();
    }

    protected abstract void configureKafka();
}
