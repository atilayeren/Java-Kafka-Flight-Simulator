package world.kafka;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.SynchronousQueue;

public class KafkaProducer extends AbstractKafkaClient {
    private final SynchronousQueue<String> messageQueue;
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    public KafkaProducer(String topicName) {
        super(topicName);
        this.messageQueue = new SynchronousQueue<>();
    }

    @Override
    public void configureKafka() {
        // Initialize properties
        Properties props = new Properties();

        // Configure the producer
        props.put("bootstrap.servers", BrokerIPAddress); // Kafka broker addresses
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // Serializer for message keys
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // Serializer for message values

        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = this.messageQueue.take();
                producer.send(new ProducerRecord<>(this.topicName, message));
                //System.out.printf("Message sent to %s: %s\n", this.topicName, message);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void push(String input) {
        try {
            this.messageQueue.put(input);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
