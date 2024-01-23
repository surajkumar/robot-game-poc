package io.github.surajkumar;

import io.github.surajkumar.client.game.Player;
import io.github.surajkumar.client.game.PlayerControls;

import java.awt.*;
import java.util.List;

public class MyCustomBot extends Bot {
    @Override
    public void performAction(Player player, PlayerControls controls, List<Player> players) {

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
