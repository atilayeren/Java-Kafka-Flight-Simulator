package world;

import com.fasterxml.jackson.databind.ObjectMapper;
import world.entity.CameraLosStatus;
import world.entity.TargetBearing;
import world.kafka.KafkaConsumer;
import world.kafka.KafkaProducer;

import java.util.ArrayList;
import java.util.List;

public class CameraControl implements Runnable {
    private static final KafkaConsumer targetBearingPositionConsumer = new KafkaConsumer("TargetBearingPosition");
    private static final KafkaProducer cameraLosStatusProducer = new KafkaProducer("CameraLosStatus");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        List<Thread> taskThreads = new ArrayList<>();
        taskThreads.add(new Thread(targetBearingPositionConsumer));
        taskThreads.add(new Thread(cameraLosStatusProducer));
        taskThreads.add(new Thread(new CameraControl()));

        for (Thread task : taskThreads)
            task.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                TargetBearing targetBearingPosition = objectMapper.readValue(targetBearingPositionConsumer.take(), TargetBearing.class);

                CameraLosStatus viewpoint = doCalculations(targetBearingPosition);

                cameraLosStatusProducer.push(objectMapper.writeValueAsString(viewpoint));

                Thread.sleep(10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private CameraLosStatus doCalculations(TargetBearing targetBearingPosition) {
        double deltaY = Math.sin(targetBearingPosition.getAngle()) * targetBearingPosition.getTargetDistance();
        double deltaX = targetBearingPosition.getTowerDistance() - Math.cos(targetBearingPosition.getAngle()) * targetBearingPosition.getTargetDistance();
        double radian = Math.atan(deltaY / deltaX);
        double degree = Math.toDegrees(radian);

        CameraLosStatus cameraLosStatus = new CameraLosStatus();

        if (!Double.isNaN(degree))
            cameraLosStatus.setViewpoint(degree);

        return cameraLosStatus;
    }
}
