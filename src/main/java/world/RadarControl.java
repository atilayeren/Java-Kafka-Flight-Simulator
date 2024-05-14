package world;

import com.fasterxml.jackson.databind.ObjectMapper;
import world.entity.TargetBearing;
import world.entity.TargetPointPosition;
import world.entity.TowerPosition;
import world.kafka.KafkaConsumer;
import world.kafka.KafkaProducer;

import java.util.ArrayList;
import java.util.List;

public class RadarControl implements Runnable {
    private static final KafkaConsumer targetPointPositionConsumer = new KafkaConsumer("TargetPointPosition");
    private static final KafkaConsumer towerPositionConsumer = new KafkaConsumer("TowerPosition");
    private static final KafkaProducer targetBearingPositionProducer = new KafkaProducer("TargetBearingPosition");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        List<Thread> taskThreads = new ArrayList<>();
        taskThreads.add(new Thread(targetPointPositionConsumer));
        taskThreads.add(new Thread(towerPositionConsumer));
        taskThreads.add(new Thread(targetBearingPositionProducer));
        taskThreads.add(new Thread(new RadarControl()));

        for (Thread task : taskThreads)
            task.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                TargetPointPosition target = objectMapper.readValue(targetPointPositionConsumer.take(), TargetPointPosition.class);
                TowerPosition tower = objectMapper.readValue(towerPositionConsumer.take(), TowerPosition.class);

                // do math
                TargetBearing targetBearing = doCalculations(target, tower);

                targetBearingPositionProducer.push(objectMapper.writeValueAsString(targetBearing));

                Thread.sleep(10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static TargetBearing doCalculations(TargetPointPosition target, TowerPosition tower) {
        double deltaTargetX = (double) target.getCoordinateX() - (double) tower.getRadarCoordinateX();
        double deltaTargetY = (double) target.getCoordinateY() - (double) tower.getRadarCoordinateY();

        double targetDistance = Math.sqrt(deltaTargetX * deltaTargetX - deltaTargetY * deltaTargetY);

        double angle = Math.asin(Math.abs(deltaTargetY) / targetDistance);

        double deltaTowerX = Math.abs((double) tower.getRadarCoordinateX() - (double) tower.getCameraCoordinateX());

        if (Double.isNaN(angle) || Double.isNaN(deltaTargetX) || Double.isNaN(targetDistance))
            return new TargetBearing();

        return new TargetBearing(angle, deltaTowerX, targetDistance);
    }
}
