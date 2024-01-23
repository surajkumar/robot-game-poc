package io.github.surajkumar.client.ui.interfaces.game;

import io.github.surajkumar.client.game.Bullet;
import io.github.surajkumar.client.game.Player;
import io.github.surajkumar.client.sound.SoundPlayer;
import io.github.surajkumar.client.ui.DrawingPanel;
import io.github.surajkumar.client.ui.GameBackground;
import io.github.surajkumar.client.ui.interfaces.GameInterface;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePlayScreen implements GameInterface {
    private static final int PLAYER_SIZE = 25;
    private static final int BULLET_SIZE = 5;
    private static final SoundPlayer SOUND_PLAYER = new SoundPlayer();
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private int playerId;

    public GamePlayScreen() {
        SOUND_PLAYER.playSound();
        playerId = -1;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    @Override
    public void draw(Graphics2D g2d, DrawingPanel panel) {
        setBackgroundImage(g2d, panel);
        handleDeath();
        drawPlayers(g2d);
        drawNameAndHealth(g2d);
        drawBullets(g2d, panel.getWidth(), panel.getHeight());
    }

    private void setBackgroundImage(Graphics2D g2d, DrawingPanel panel) {
        BufferedImage backgroundImage = GameBackground.getBackgroundImage();
        if(backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        }
    }

    private void drawPlayers(Graphics2D g2d) {
        for (Player player : Collections.unmodifiableList(players)) {
            player.updateOrientation();
            AffineTransform oldTransform = g2d.getTransform();

            g2d.translate(player.getLocation().getX() + PLAYER_SIZE / 2, player.getLocation().getY() + PLAYER_SIZE / 2);
            g2d.rotate(Math.toRadians(player.getLocation().getOrientation()));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, player.getAlpha()));
            g2d.setColor(player.getColor());
            g2d.fillRect(-PLAYER_SIZE / 2, -PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
            g2d.setTransform(oldTransform);

            int lineLength = 1080;
            double radian = Math.toRadians(player.getLocation().getOrientation());
            int endX = player.getLocation().getX() + PLAYER_SIZE / 2 + (int) (lineLength * Math.cos(radian));
            int endY = player.getLocation().getY() + PLAYER_SIZE / 2 + (int) (lineLength * Math.sin(radian));
            g2d.setColor(player.getColor());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(player.getLocation().getX() + PLAYER_SIZE / 2, player.getLocation().getY() + PLAYER_SIZE / 2, endX, endY);
        }
    }

    private void drawNameAndHealth(Graphics2D g2d) {
        for (Player player : Collections.unmodifiableList(players)) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(player.getName(), player.getLocation().getX(), player.getLocation().getY() + PLAYER_SIZE + 10);
            g2d.drawString("Health: " + player.getHealth(), player.getLocation().getX(), player.getLocation().getY() + PLAYER_SIZE + 20);
        }
    }

    private void handleDeath() {
        synchronized (players) {
            List<Player> playersToRemove = new ArrayList<>();
            for (Player player : players) {
                if(player.isDead()) {
                    if (!player.isInDeathAnimation()) {
                        player.startDeathAnimation();
                    }
                    player.updateDeathAnimation();
                    if (player.getAlpha() <= 0) {
                        playersToRemove.add(player);
                    }
                }
            }
            players.removeAll(playersToRemove);
        }
    }

    private void drawBullets(Graphics2D g2d, int panelWith, int panelHeight) {
        synchronized (bullets) {
            List<Bullet> bulletsToRemove = new ArrayList<>();
            Map<Color, List<Bullet>> bulletsByColor = new HashMap<>();
            for (Bullet bullet : bullets) {
                Player player = getPlayerById(bullet.getPlayerId());
                bulletsByColor.computeIfAbsent(player.getColor(), k -> new ArrayList<>()).add(bullet);
                if (bullet.getX() > panelWith || bullet.getX() < 0 || bullet.getY() < 0 || bullet.getY() > panelHeight) {
                    bulletsToRemove.add(bullet);
                } else {
                    bullet.updatePosition();
                    for (Player enemy : players) {
                        if (enemy != player && checkCollision(bullet, enemy)) {
                            bulletsToRemove.add(bullet);
                            break;
                        }
                    }
                }
            }
            for (Map.Entry<Color, List<Bullet>> entry : bulletsByColor.entrySet()) {
                g2d.setColor(entry.getKey());
                for (Bullet bullet : entry.getValue()) {
                    if(bullet.getPlayerId() == playerId) {
                        SOUND_PLAYER.playSoundEffect();
                    }
                    g2d.fillRect(bullet.getX() + PLAYER_SIZE / 2 - 2, bullet.getY() + PLAYER_SIZE / 2 - 2, BULLET_SIZE, BULLET_SIZE);
                }
            }
            bullets.removeAll(bulletsToRemove);
        }
    }

    private boolean checkCollision(Bullet bullet, Player enemy) {
        Rectangle missileRect = new Rectangle(bullet.getX(), bullet.getY(), BULLET_SIZE, BULLET_SIZE);
        Rectangle enemyRect = new Rectangle(enemy.getLocation().getX(), enemy.getLocation().getY(), PLAYER_SIZE, PLAYER_SIZE);
        return missileRect.intersects(enemyRect);
    }

    private Player getPlayerById(int playerId) {
        return players.stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
