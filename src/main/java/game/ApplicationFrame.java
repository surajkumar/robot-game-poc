package game;

import javax.swing.*;

public class ApplicationFrame {

    private final JFrame frame;
    private final GamePanel gamePanel;

    public ApplicationFrame() {
        gamePanel = new GamePanel();
        frame = new JFrame("Robot Game");
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.add(gamePanel);
        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
