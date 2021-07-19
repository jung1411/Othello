package main.example.models;

public final class Direction {

    private int deltaX, deltaY;

    public Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "deltaX=" + deltaX +
                ", deltaY=" + deltaY +
                '}';
    }
}
