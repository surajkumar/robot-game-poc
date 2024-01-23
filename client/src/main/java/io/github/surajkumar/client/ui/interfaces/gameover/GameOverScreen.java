package io.github.surajkumar.client.ui.interfaces.gameover;

import io.github.surajkumar.client.ui.DrawingPanel;
import io.github.surajkumar.client.ui.interfaces.GameInterface;

import java.awt.*;

public class GameOverScreen implements GameInterface {
    @Override
    public void draw(Graphics2D g2d, DrawingPanel panel) {
        g2d.setColor(Color.RED);
        g2d.drawString("Game Over", 500, 500);
    }
}
