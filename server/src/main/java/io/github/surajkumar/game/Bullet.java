package io.github.surajkumar.game;

public class Bullet {
    private static final int DRAW_SPEED = 10;

    private int x;
    private int y;
    private final int dx;
    private final int dy;
    private final double orientation;
    private final int playerId;

    public Bullet(int playerId, int x, int y, double orientation) {
        this.playerId = playerId;
        this.orientation = orientation;
        this.dx = (int) (DRAW_SPEED * Math.cos(Math.toRadians(orientation)));
        this.dy = (int) (DRAW_SPEED * Math.sin(Math.toRadians(orientation)));
        this.x = x;
        this.y = y;
    }

    public void updateLocation() {
        x += dx;
        y += dy;
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

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public double getOrientation() {
        return orientation;
    }

    public int getPlayerId() {
        return playerId;
    }
}
