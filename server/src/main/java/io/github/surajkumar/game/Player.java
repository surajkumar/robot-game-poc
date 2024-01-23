package io.github.surajkumar.game;

import io.vertx.core.net.NetSocket;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private final NetSocket socket;
    private final int id;
    private final String name;
    private final Color color;
    private Location location;
    private final List<Location> path = new CopyOnWriteArrayList<>();
    private int health;
    private final Weapon weapon;

    public Player(NetSocket socket, int id, String name, Color color, Location location, int health) {
        this.socket = socket;
        this.id = id;
        this.name = name;
        this.color = color;
        this.location = location;
        this.health = health;
        this.weapon = new Weapon(10, 10, 500);
    }

    public Location getLastLocation() {
        if(path.isEmpty()) {
            return location;
        }
        return path.getLast();
    }

    public void walk(List<Location> nodes) {
        path.addAll(nodes);
    }

    public void walk() {
        if (!path.isEmpty()) {
            Location destination = path.getLast();
            if (destination != null && (destination.x() != location.x() || destination.y() != location.y())) {
                Location next = path.getFirst();
                path.remove(next);
                location = next;
            }
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public boolean isDead() {
        return health <= 0;
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

    public NetSocket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
