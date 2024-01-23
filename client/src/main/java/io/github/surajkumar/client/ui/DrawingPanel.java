package io.github.surajkumar.client.ui;

import io.github.surajkumar.client.ui.interfaces.GameInterface;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    private GameInterface gameInterface;

    public DrawingPanel() {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameInterface != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            gameInterface.draw(g2d, this);
            g2d.dispose();
        }
    }

    public void showInterface(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }
}
