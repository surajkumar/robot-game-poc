package io.github.surajkumar;

import io.github.surajkumar.client.game.Bullet;
import io.github.surajkumar.client.game.Location;
import io.github.surajkumar.client.game.Player;
import io.github.surajkumar.client.game.PlayerControls;
import io.github.surajkumar.client.ui.ApplicationFrame;
import io.github.surajkumar.client.ui.interfaces.game.GamePlayScreen;
import io.github.surajkumar.client.ui.interfaces.gameover.GameOverScreen;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Bot {
    public abstract void performAction(Player player, PlayerControls controls, List<Player> players);
    public abstract String getName();
    public abstract Color getColor();

    public void connectToServer(String host, int port) {
        Vertx vertx = Vertx.vertx();
        NetClientOptions options = new NetClientOptions().setReceiveBufferSize(90000);
        NetClient client = vertx.createNetClient(options);
        client.connect(43594, "localhost").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("Connected successfully to server");
                setup(res.result());
            } else {
                System.err.println("Failed to connect: " + res.cause().getMessage());
            }
        });
    }

    private void registerWithServer(NetSocket socket, String name, int red, int green, int blue) {
        int rgb = (red << 16) | (green << 8) | blue;
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(0);
        buffer.appendInt(rgb);
        buffer.appendString(name);
        socket.write(buffer);
    }

    private void setup(NetSocket socket) {
        GamePlayScreen gamePlayScreen = new GamePlayScreen();
        ApplicationFrame application = new ApplicationFrame();
        application.getGamePanel().showInterface(gamePlayScreen);

        socket.handler(buffer -> {

            int index = 0;

            while(index != buffer.length()) {

                int opcode = buffer.getInt(index);
                index += 4;

                if (opcode == 0) {
                    int totalPlayers = buffer.getInt(index);
                    index += 4;

                    for (int i = 0; i < totalPlayers; i++) {
                        int color = buffer.getInt(index);
                        index += 4;

                        int x = buffer.getInt(index);
                        index += 4;

                        int y = buffer.getInt(index);
                        index += 4;

                        int health = buffer.getInt(index);
                        index += 4;

                        int id = buffer.getInt(index);
                        index += 4;

                        double orientation = buffer.getDouble(index);
                        index += 8;

                        int nameLen = buffer.getInt(index);
                        index += 4;

                        String name = buffer.getString(index, index + nameLen);
                        index += nameLen;

                        int red = (color >> 16) & 0xFF;
                        int green = (color >> 8) & 0xFF;
                        int blue = color & 0xFF;

                        Player player = new Player(id, name, new Color(red, green, blue), new Location(x, y, orientation), health);
                        PlayerControls controls = new PlayerControls(player, socket);

                        if(gamePlayScreen.getPlayerId() == -1) {
                            gamePlayScreen.setPlayerId(id);
                        }
                        gamePlayScreen.addPlayer(player);

                        new Timer(100, e -> performAction(player, controls, gamePlayScreen.getPlayers())).start();
                    }

                    application.show();

                } else if (opcode == 2) {
                    int playerId = buffer.getInt(index);

                    index += 4;

                    int x = buffer.getInt(index);
                    index += 4;

                    int y = buffer.getInt(index);
                    index += 4;

                    double orientation = buffer.getDouble(index);
                    index += 8;

                    int health = buffer.getInt(index);
                    index += 4;

                    Player player = gamePlayScreen
                            .getPlayers()
                            .stream()
                            .filter(p -> p.getId() == playerId)
                            .findFirst()
                            .orElse(null);

                    if (player != null) {
                        player.getLocation().setX(x);
                        player.getLocation().setY(y);
                        player.getLocation().setTargetOrientation(orientation);
                        player.setHealth(health);
                    }
                } else if (opcode == 3) {
                    int color = buffer.getInt(index);
                    index += 4;

                    int x = buffer.getInt(index);
                    index += 4;

                    int y = buffer.getInt(index);
                    index += 4;

                    int health = buffer.getInt(index);
                    index += 4;

                    int id = buffer.getInt(index);
                    index += 4;

                    double orientation = buffer.getDouble(index);
                    index += 8;

                    int nameLen = buffer.getInt(index);
                    index += 4;

                    String name = buffer.getString(index, index + nameLen);
                    index += nameLen;

                    int red = (color >> 16) & 0xFF;
                    int green = (color >> 8) & 0xFF;
                    int blue = color & 0xFF;

                    Player player = new Player(id, name, new Color(red, green, blue), new Location(x, y, orientation), health);
                    gamePlayScreen.addPlayer(player);

                } else if (opcode == 4) {
                    int playerId = buffer.getInt(index);
                    index += 4;

                    gamePlayScreen
                            .getPlayers()
                            .stream()
                            .filter(p -> p.getId() == playerId)
                            .findFirst().ifPresent(player -> player.setDead(true));


                } else if (opcode == 5) {
                    int playerId = buffer.getInt(index);
                    index += 4;

                    int amount = buffer.getInt(index);
                    index += 4;

                    int x = buffer.getInt(index);
                    index += 4;

                    int y = buffer.getInt(index);
                    index += 4;

                    int dx = buffer.getInt(index);
                    index += 4;

                    int dy = buffer.getInt(index);
                    index += 4;

                    for(int i = 0; i < amount; i++) {
                        Bullet bullet = new Bullet(playerId, x, y, dx, dy);
                        gamePlayScreen.addBullet(bullet);
                    }
                } else if (opcode == 6) {
                    application.getGamePanel().showInterface(new GameOverScreen());
                }
            }
        });

        if(getColor() == null) {
            System.err.println("You need to return a valid Color within the getColor() method");
            System.exit(0);
            return;
        }

        if(getName() == null) {
            System.err.println("You need to return a name for yourself in the getName() method");
            return;
        }

        registerWithServer(socket, getName(), getColor().getRed(), getColor().getGreen(), getColor().getBlue());
    }
}
