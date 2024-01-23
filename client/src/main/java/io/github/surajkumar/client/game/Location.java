package io.github.surajkumar.client.game;

public class Location {
    private int x;
    private int y;
    private double orientation;
    private double targetOrientation;
    private int dx;
    private int dy;

    public Location(int x, int y, double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getTargetOrientation() {
        return targetOrientation;
    }

    public void setTargetOrientation(double targetOrientation) {
        this.targetOrientation = targetOrientation;
    }
}
