package world.entity;

public class TowerPosition {
    private int cameraCoordinateX;
    private int cameraCoordinateY;
    private int radarCoordinateX;
    private int radarCoordinateY;

    public TowerPosition() {
        this.cameraCoordinateX = 0;
        this.cameraCoordinateY = 0;
        this.radarCoordinateX = 0;
        this.radarCoordinateY = 0;
    }

    public TowerPosition(int cameraCoordinateX, int cameraCoordinateY, int radarCoordinateX, int radarCoordinateY) {
        this.cameraCoordinateX = cameraCoordinateX;
        this.cameraCoordinateY = cameraCoordinateY;
        this.radarCoordinateX = radarCoordinateX;
        this.radarCoordinateY = radarCoordinateY;
    }

    public int getCameraCoordinateX() {
        return cameraCoordinateX;
    }

    public void setCameraCoordinateX(int cameraCoordinateX) {
        this.cameraCoordinateX = cameraCoordinateX;
    }

    public int getCameraCoordinateY() {
        return cameraCoordinateY;
    }

    public void setCameraCoordinateY(int cameraCoordinateY) {
        this.cameraCoordinateY = cameraCoordinateY;
    }

    public int getRadarCoordinateX() {
        return radarCoordinateX;
    }

    public void setRadarCoordinateX(int radarCoordinateX) {
        this.radarCoordinateX = radarCoordinateX;
    }

    public int getRadarCoordinateY() {
        return radarCoordinateY;
    }

    public void setRadarCoordinateY(int radarCoordinateY) {
        this.radarCoordinateY = radarCoordinateY;
    }
}
