package world.entity;

public class CameraLosStatus {

    private double viewpoint;

    public CameraLosStatus(double viewpoint) {
        this.viewpoint = viewpoint;
    }

    public CameraLosStatus() {
        this.viewpoint = 0;
    }

    public double getViewpoint() {
        return viewpoint;
    }

    public void setViewpoint(double viewpoint) {
        this.viewpoint = viewpoint;
    }
}
