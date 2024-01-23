package io.github.surajkumar.client.game;

import javax.swing.*;
import java.awt.*;

public class Player {
    private final int id;
    private final String name;
    private final Color color;
    private final Location location;
    private int health;
    private boolean dead;

    private Location targetLocation;

    public void setTarget(Location location) {
        this.targetLocation = location;
    }

    public boolean isWalking() {
        return targetLocation.getX() != location.getX() && targetLocation.getY() != location.getY();
    }

    public void updateOrientation() {
        double currentOrientation = location.getOrientation();
        double targetOrientation = location.getTargetOrientation();
        double rotationSpeed = 8;
        double angleDifference = targetOrientation - currentOrientation;

        angleDifference -= Math.floor(angleDifference / 360 + 0.5) * 360;

        double adjustedRotationSpeed = Math.min(rotationSpeed, Math.abs(angleDifference));

        if (Math.abs(angleDifference) > 0) {
            if (angleDifference > 0) {
                currentOrientation += adjustedRotationSpeed;
            } else {
                currentOrientation -= adjustedRotationSpeed;
            }
            currentOrientation = (currentOrientation + 360) % 360;
            location.setOrientation(currentOrientation);
        }
    }

    public Player(int id, String name, Color color, Location location, int health) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.location = location;
        this.targetLocation = location;
        this.health = health;
    }

    private boolean inDeathAnimation = false;
    private float alpha = 1.0f;
    private Timer deathAnimationTimer;
    public boolean isInDeathAnimation() {
        return inDeathAnimation;
    }

    public void startDeathAnimation() {
        if (inDeathAnimation) {
            return;
        }
        inDeathAnimation = true;
        deathAnimationTimer = new Timer(100, e -> {
            alpha -= 0.1f;
            if (alpha <= 0) {
                alpha = 0;
                deathAnimationTimer.stop();
            }
        });
        deathAnimationTimer.start();
    }

    public void updateDeathAnimation() {
        if (inDeathAnimation) {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
            }
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isDead() {
        return dead;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Location getLocation() {
        return location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getId() {
        return id;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
