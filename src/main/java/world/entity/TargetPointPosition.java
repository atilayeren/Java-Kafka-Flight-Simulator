package world.entity;

public class TargetPointPosition {
    private int coordinateX;
    private int coordinateY;

    public TargetPointPosition() {
        this.coordinateX = 0;
        this.coordinateY = 0;
    }

    public TargetPointPosition(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return this.coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return this.coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }
}
