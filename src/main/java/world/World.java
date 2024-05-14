package world;

import com.fasterxml.jackson.databind.ObjectMapper;
import world.entity.CameraLosStatus;
import world.kafka.KafkaConsumer;
import world.kafka.KafkaProducer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final KafkaProducer targetPointPositionProducer = new KafkaProducer("TargetPointPosition");
    private static final KafkaProducer towerPositionProducer = new KafkaProducer("TowerPosition");
    private static final KafkaConsumer cameraLosStatusConsumer = new KafkaConsumer("CameraLosStatus");

    public static void main(String[] args) {
        List<Thread> taskThreads = new ArrayList<>();
        taskThreads.add(new Thread(targetPointPositionProducer));
        taskThreads.add(new Thread(towerPositionProducer));
        taskThreads.add(new Thread(cameraLosStatusConsumer));

        for (Thread task : taskThreads)
            task.start();

        SwingUtilities.invokeLater(() -> new WorldGui().setVisible(true));

        while (true) {
            try {
                if (WorldGui.simulationActive){
                    targetPointPositionProducer.push(objectMapper.writeValueAsString(WorldGui.target));
                    towerPositionProducer.push(objectMapper.writeValueAsString(WorldGui.tower));

                    WorldGui.cameraViewpoint = objectMapper.readValue(cameraLosStatusConsumer.take(), CameraLosStatus.class);
                }
                else{
                    // poll old messages to clean topics
                    cameraLosStatusConsumer.poll();
                }

                Thread.sleep(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
