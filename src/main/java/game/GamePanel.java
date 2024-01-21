package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private static final int ROBOT_SIZE = 25;
    private final List<GameRobot> robots = new ArrayList<>();

    private BufferedImage backgroundImage;


    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        // Timer for repainting
        Timer repaintTimer = new Timer(16, e -> repaint());
        repaintTimer.start();

        // Timer for updating game state
        Timer updateTimer = new Timer(16, e -> updateGameState());
        updateTimer.start();

        try {
            backgroundImage = ImageIO.read(new File("./bg_01.png"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        synchronized (robots) {

            List<GameRobot> robots1 = new ArrayList<>(robots);

            for (GameRobot robot : robots1) {
                robot.updateOrientation();

                if (robot.getHealth() <= 0 && !robot.isInDeathAnimation()) {
                    robot.startDeathAnimation();
                }

                robot.updateDeathAnimation();
                if (robot.getAlpha() <= 0) {
                    robots.remove(robot);
                    continue;
                }

                // Save the current transform
                AffineTransform oldTransform = g2d.getTransform();

                // Translate and rotate the graphics context
                g2d.translate(robot.getX() + ROBOT_SIZE / 2, robot.getY() + ROBOT_SIZE / 2);
                g2d.rotate(Math.toRadians(robot.getOrientation()));

                // Draw the robot (now rotated around its center)
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, robot.getAlpha()));
                g2d.setColor(robot.getColor());
                g2d.fillRect(-ROBOT_SIZE / 2, -ROBOT_SIZE / 2, ROBOT_SIZE, ROBOT_SIZE); // Note the adjusted coordinates

                g2d.setTransform(oldTransform);

                int lineLength = 1080; // Length of the direction line
                double radian = Math.toRadians(robot.getOrientation());
                int endX = robot.getX() + ROBOT_SIZE / 2 + (int) (lineLength * Math.cos(radian));
                int endY = robot.getY() + ROBOT_SIZE / 2 + (int) (lineLength * Math.sin(radian));

                // Draw the direction line
                g2d.setColor(robot.getColor()); // Line color
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(robot.getX() + ROBOT_SIZE / 2, robot.getY() + ROBOT_SIZE / 2, endX, endY);


                // Reset to the original transform for drawing text


                // Draw the name and health
                g2d.setColor(Color.WHITE);
                g2d.drawString(robot.getName(), robot.getX(), robot.getY() + ROBOT_SIZE + 10);
                g2d.drawString("Health: " + robot.getHealth(), robot.getX(), robot.getY() + ROBOT_SIZE + 20);

                // Draw missiles
                synchronized (robot.getMissiles()) {

                    List<Missile> missiles = new ArrayList<>(robot.getMissiles());

                    for (Missile missile : missiles) {
                        g2d.setColor(robot.getColor());
                        g2d.fillRect(missile.getX() + ROBOT_SIZE / 2 - 2, missile.getY() + ROBOT_SIZE / 2 - 2, 5, 5);
                        if(missile.getX() > 1080 || missile.getX() < 0 || missile.getY() < 0 || missile.getY() > 720) {
                            robot.removeMissile(missile);
                        }
                    }
                }
            }
        }

        g2d.dispose();
    }



    private void updateGameState() {
        synchronized (robots) {
            for (GameRobot robot : robots) {
                synchronized (robot.getMissiles()) {
                    for (Missile missile : robot.getMissiles()) {
                        missile.updatePosition();
                        for (GameRobot enemy : robots) {
                            if (enemy != robot && checkCollision(missile, enemy)) {
                                enemy.setHealth(enemy.getHealth() - 1);
                                robot.removeMissile(missile);
                            }
                        }
                    }
                }
            }
            repaint();
        }
    }

    private boolean checkCollision(Missile missile, GameRobot enemy) {
        Rectangle missileRect = new Rectangle(missile.getX(), missile.getY(), 5, 5);
        Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), ROBOT_SIZE, ROBOT_SIZE);
        return missileRect.intersects(enemyRect);
    }


    public void addRobot(GameRobot robot) {
        if(!robots.contains(robot)) {
            robots.add(robot);
        }
    }

    public void removeRobot(GameRobot robot) {
        synchronized (robots) {
            robots.remove(robot);
        }
    }
}
