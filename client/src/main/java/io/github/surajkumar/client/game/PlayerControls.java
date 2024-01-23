package io.github.surajkumar.client.game;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayerControls implements Runnable {
    private final Player player;
    private final NetSocket socket;

    private final Queue<Buffer> queue = new LinkedBlockingDeque<>();

    public PlayerControls(Player player, NetSocket socket) {
        this.player = player;
        this.socket = socket;
        new Thread(this).start();
    }

    public void moveTo(int targetX, int targetY) {
        player.setTarget(new Location(targetX, targetY, 0));
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(1);
        buffer.appendInt(targetX);
        buffer.appendInt(targetY);
        buffer.appendDouble(player.getLocation().getOrientation());
        queue.add(buffer);
    }

    public void rotate(double angle) {
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(8);
        buffer.appendDouble(angle);
        queue.add(buffer);
    }

    public boolean shoot() {
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(5);
        queue.add(buffer);
        return true;
    }

    public void reload() {
        //TODO: Send reload request to server
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(!queue.isEmpty()) {
                    Buffer next = queue.poll();
                    socket.write(next);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
