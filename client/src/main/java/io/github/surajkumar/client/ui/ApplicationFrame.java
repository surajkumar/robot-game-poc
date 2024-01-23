package io.github.surajkumar.client.ui;

import javax.swing.*;

public class ApplicationFrame {

    private final JFrame frame;
    private final DrawingPanel gamePanel;
    public ApplicationFrame() {
        gamePanel = new DrawingPanel();
        frame = new JFrame("Game");
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.add(gamePanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.addKeyListener(gamePanel);
        //frame.addMouseListener(gamePanel);
        new Timer(16, e -> gamePanel.repaint()).start();
    }

    public void show() {
        frame.setVisible(true);
    }

    public DrawingPanel getGamePanel() {
        return gamePanel;
    }
}
