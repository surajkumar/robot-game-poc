package io.github.surajkumar;

import io.github.surajkumar.client.game.Player;
import io.github.surajkumar.client.game.PlayerControls;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyCustomBot extends Bot {
    @Override
    public void performAction(Player player, PlayerControls controls, List<Player> players) {

        controls.moveTo(ThreadLocalRandom.current().nextInt(800), ThreadLocalRandom.current().nextInt(600));
        controls.shoot();
        controls.shoot();
        controls.shoot();
        controls.shoot();
    }

    @Override
    public String getName() {
        return "Wazei";
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }
}
