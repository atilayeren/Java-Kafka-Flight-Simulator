package world.kafka;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.SynchronousQueue;

import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumer extends AbstractKafkaClient {
    private final SynchronousQueue<String> messageQueue;
    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    public KafkaConsumer(String topicName) {
        super(topicName);
        this.messageQueue = new SynchronousQueue<>();
    }

    @Override
    protected void configureKafka() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BrokerIPAddress);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singletonList(this.topicName));
    }

    @Override
    public void run() {
        while (true) {
            try {
                ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    this.messageQueue.put(record.value());
                    System.out.printf("Message received from %s: %s\n", this.topicName, record.value());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String take() throws InterruptedException {
        return this.messageQueue.take();
    }
    public void poll() {
        this.messageQueue.poll();
    }
}
