package game;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameRobot implements Runnable {
    private int x;
    private int y;
    private final Color color;
    private final String name;
    private final List<Missile> missiles = new CopyOnWriteArrayList<>();
    private int ammo = 5;

    private int health;
    private float alpha = 1.0f;
    private boolean inDeathAnimation = false;
    private javax.swing.Timer deathAnimationTimer;

    private double targetOrientation = 0;
    private double orientation = 0;
    private double rotationSpeed = 10; // Degrees per update


    public void setTargetOrientation(double targetOrientation) {
        this.targetOrientation = targetOrientation;
    }

    public double getOrientation() {
        return orientation;
    }

    public void updateOrientation() {
        if (Math.abs(targetOrientation - orientation) > rotationSpeed) {
            if (targetOrientation > orientation) {
                orientation += rotationSpeed;
            } else {
                orientation -= rotationSpeed;
            }
           // orientation = (orientation + 360) % 360; // Normalize the orientation
        }
    }

    public void rotate(double angle) {
        orientation += angle;
        orientation = orientation % 360; // Keep the orientation within 0-359 degrees
    }

    public void startDeathAnimation() {
        if (inDeathAnimation) return; // Prevent restarting the animation

        inDeathAnimation = true;

        deathAnimationTimer = new javax.swing.Timer(100, e -> {
            alpha -= 0.1f; // Decrease alpha value
            if (alpha <= 0) {
                alpha = 0;
                deathAnimationTimer.stop();
                // Optionally, mark this robot for removal from the game
            }
        });

        deathAnimationTimer.start();
    }

    public boolean isInDeathAnimation() {
        return inDeathAnimation;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void updateDeathAnimation() {
        if (inDeathAnimation) {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
            }
        }
    }

    public GameRobot(Color color, String name) {
        this.x = ThreadLocalRandom.current().nextInt(0, 1080 - 25);
        this.y = ThreadLocalRandom.current().nextInt(0, 720 - 25);
        this.color = color;
        this.name = name;
        this.health = 10;
    }

    public void moveTo(int targetX, int targetY) {
        while (x != targetX && y != targetY) {
            int xMove = (int) Math.signum(targetX - x);
            int yMove = (int) Math.signum(targetY - y);

            // Calculate the angle of movement
            double angle = Math.atan2(yMove, xMove);
            // Convert the angle from radians to degrees and update orientation
            setTargetOrientation(Math.toDegrees(angle));

            x += xMove;
            y += yMove;
            sleepRandom(0, 2);
        }
    }


    private void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveRight() {
        x++;
    }

    public void moveLeft() {
        if(x <= 0) {
            return;
        }
        x--;
    }

    public void moveUp() {
        if(y <= 0) {
            return;
        }
        y--;
    }

    public void moveDown() {
        y++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public boolean fireMissile() {
        if (ammo == 0) {
            return false;
        }
        ammo--;
        double radian = Math.toRadians(orientation);

        // Slightly vary the missile speed
        int baseMissileSpeed = 10;
        int speedVariation = ThreadLocalRandom.current().nextInt(-2, 3); // Varies speed by -2 to 2
        int missileSpeed = baseMissileSpeed + speedVariation;

        int dx = (int) (missileSpeed * Math.cos(radian));
        int dy = (int) (missileSpeed * Math.sin(radian));

        Missile newMissile = new Missile(this.x, this.y, dx, dy);
        this.getMissiles().add(newMissile);
        return true;
    }


    public void reload() {
        ammo = 5;
    }

    @Override
    public void run() {
        while(!inDeathAnimation) {


            moveTo(ThreadLocalRandom.current().nextInt(0, 1080 - 50), ThreadLocalRandom.current().nextInt(0, 720 - 50));


            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
            fireMissile();
          //  moveTo(ThreadLocalRandom.current().nextInt(0, 1080 - 50), ThreadLocalRandom.current().nextInt(0, 720 - 50));


            fireMissile();

            if(!fireMissile()) {
                reload();
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sleepRandom(int low, int high) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(low,high));
        } catch (InterruptedException ignore) {
            /* Ignore */
        }
    }

    public void removeMissile(Missile missile) {
        missiles.remove(missile);
    }


    public List<Missile> getMissiles() {
        return missiles;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
