package io.github.surajkumar.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Weapon {
    private int magazineSize;
    private int ammo;
    private final List<Bullet> bulletsToQueue = Collections.synchronizedList(new ArrayList<>());
    private int speed;
    private long lastShot = System.currentTimeMillis();

    public Weapon(int magazineSize, int ammo, int speed) {
        this.magazineSize = magazineSize;
        this.ammo = ammo;
        this.speed = speed;
    }

    public void shoot(int playerId, Location location) {
        if((System.currentTimeMillis() - lastShot) < speed) {
            return;
        }
        if(ammo <= 0) {
            reload();
            return;
        }
        ammo--;
        synchronized (bulletsToQueue) {
            bulletsToQueue.add(new Bullet(playerId, location.x(), location.y(), location.orientation()));
        }
        lastShot = System.currentTimeMillis();
    }

    public void reload() {
        ammo = magazineSize;
    }

    public int getMagazineSize() {
        return magazineSize;
    }

    public void setMagazineSize(int magazineSize) {
        this.magazineSize = magazineSize;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public List<Bullet> getBulletsToQueue() {
        return bulletsToQueue;
    }

    public void removeBullet(Bullet bullets) {
        synchronized (bulletsToQueue) {
            bulletsToQueue.remove(bullets);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getLastShot() {
        return lastShot;
    }

    public void setLastShot(long lastShot) {
        this.lastShot = lastShot;
    }
}
