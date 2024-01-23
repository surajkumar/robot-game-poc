package io.github.surajkumar.game;

import io.github.surajkumar.server.MessageSender;
import io.github.surajkumar.server.codec.message.*;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static final Game INSTANCE = new Game();
    private final List<Player> players = new CopyOnWriteArrayList<>();

    public void addPlayer(Player player) {
        if(!players.contains(player)) {
            for(Player p : players) {
                MessageSender.sendMessage(p.getSocket(), new NewPlayerJoinedMessage(player));
            }
            players.add(player);
            LOGGER.info("Registered " + player.getId());
        }
    }

    private final Map<NetSocket, List<Message>> queue = new HashMap<>();

    @Override
    public void run() {
        sendDead();
        sendLocationUpdates();
        sendFiredBullets();
        checkBulletsHitAnybody();
        queue.forEach(MessageSender::sendMessages);
        queue.clear();
    }

    private final List<Player> deadToRemove = new ArrayList<>();

    private void sendDead() {
        synchronized (players) {
            deadToRemove.clear();
            for(Player p : players) {
                if(p.isDead()) {
                    deadToRemove.add(p);
                    MessageSender.sendMessage(p.getSocket(), new GameOverMessage());
                }
            }
            players.removeAll(deadToRemove);
            deadToRemove.forEach(this::removePlayer);
        }
    }

    private final List<Bullet> bulletsFired = Collections.synchronizedList(new ArrayList<>());

    public synchronized void sendFiredBullets() {
        List<Message> fireBulletMessages = new ArrayList<>();
        for (Player player : players) {
            int shotsFired = player.getWeapon().getBulletsToQueue().size();
            if(shotsFired > 0) {
                Bullet bullet = player.getWeapon().getBulletsToQueue().getFirst();
                fireBulletMessages.add(new FireBulletMessage(player, bullet));
                player.getWeapon().removeBullet(bullet);
                player.getWeapon().setLastShot(System.currentTimeMillis());
                bulletsFired.add(bullet);
            }
        }
        if(!fireBulletMessages.isEmpty()) {
            for (Player p : players) {
                queue.put(p.getSocket(), fireBulletMessages);
            }
        }
    }

    private void checkBulletsHitAnybody() {
        List<Bullet> bulletToRemove = new ArrayList<>();
        synchronized (bulletsFired) {
            for (Bullet bullet : bulletsFired) {
                bullet.updateLocation();
                for (Player player : players) {
                    if (bullet.getPlayerId() == player.getId()) {
                        continue;
                    }
                    if (checkCollision(new Location(bullet.getX(), bullet.getY(), bullet.getOrientation()), player)) {
                        player.setHealth(player.getHealth() - 1);
                        bulletToRemove.add(bullet);
                    }
                }
            }
            bulletsFired.removeAll(bulletToRemove);
        }
    }

    private boolean checkCollision(Location currentBulletLocation, Player player) {
        Rectangle bulletRect = new Rectangle(currentBulletLocation.x(), currentBulletLocation.y(), 5, 5);
        Rectangle playerRect = new Rectangle(player.getLocation().x(), player.getLocation().y(), 25, 25);
        return bulletRect.intersects(playerRect);
    }

    private void sendLocationUpdates() {
        List<Message> locationMessages = new ArrayList<>();
        for (Player player : players) {
            player.walk();
            locationMessages.add(new LocationUpdateMessage(player, player.getLocation()));
        }
        for (Player p : players) {
            queue.put(p.getSocket(), locationMessages);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player) {
        player.setHealth(0);
        synchronized (players) {
            players.remove(player);
        }
        for (Player p : players) {
            MessageSender.sendMessage(p.getSocket(), new PlayerDeathMessage(player));
        }
    }

    public Optional<Player> getPlayerBySocket(NetSocket socket) {
        return players
                .stream()
                .filter(p -> p.getSocket() == socket)
                .findFirst();
    }

    public static Game getInstance() {
        return INSTANCE;
    }
}
