package io.github.surajkumar.client.game;

public class Bullet {
    private final int playerId;
    private final int  dx;
    private final int dy;
    private int x;
    private int y;

    public Bullet(int playerId, int x, int y, int dx, int dy) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void updatePosition() {
        x += dx;
        y += dy;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
