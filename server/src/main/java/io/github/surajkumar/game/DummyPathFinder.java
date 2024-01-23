package io.github.surajkumar.game;

import java.util.ArrayList;
import java.util.List;

public class DummyPathFinder {

    public static List<Location> pathTo(Location current, Location target) {
        List<Location> path = new ArrayList<>();

        int x = current.x();
        int y = current.y();

        while (x != target.x() || y != target.y()) {
            int moveX = (int) Math.signum(target.x() - x);
            int moveY = (int) Math.signum(target.y() - y);
            double angle = Math.atan2(moveY, moveX);

            x += moveX;
            y += moveY;

            Location node = new Location(x, y, Math.toDegrees(angle));
            path.add(node);
        }

        return path;
    }
}
