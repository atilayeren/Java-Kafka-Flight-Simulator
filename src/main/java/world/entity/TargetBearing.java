package world.entity;

public class TargetBearing {

    private double angle;
    private double towerDistance;
    private double targetDistance;

    public TargetBearing(double angle, double towerDistance, double targetDistance) {
        this.angle = angle;
        this.towerDistance = towerDistance;
        this.targetDistance = targetDistance;
    }

    public TargetBearing() {
        this.angle = 0;
        this.towerDistance = 0;
        this.targetDistance = 0;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getTowerDistance() {
        return towerDistance;
    }

    public void setTowerDistance(double towerDistance) {
        this.towerDistance = towerDistance;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        this.targetDistance = targetDistance;
    }


}
