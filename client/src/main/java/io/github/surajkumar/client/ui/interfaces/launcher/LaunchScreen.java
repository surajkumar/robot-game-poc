package io.github.surajkumar.client.ui.interfaces.launcher;

import io.github.surajkumar.client.ui.DrawingPanel;
import io.github.surajkumar.client.ui.GameBackground;
import io.github.surajkumar.client.ui.interfaces.GameInterface;
import io.github.surajkumar.client.ui.interfaces.KeyboardListener;
import io.github.surajkumar.client.ui.interfaces.MouseListener;
import io.github.surajkumar.client.ui.interfaces.game.GamePlayScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class LaunchScreen implements GameInterface, MouseListener, KeyboardListener {
    private String name = "";
    private boolean isNameFieldSelected = false;
    private DrawingPanel panel;

    @Override
    public void draw(Graphics2D g2d, DrawingPanel panel) {
        this.panel = panel;
        g2d.drawImage(GameBackground.getLaunchBg().getImage(), 0, 0, panel.getWidth(), panel.getHeight(), null);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(50, 100, 200, 30);
        g2d.drawString("Your Name: " + name, 55, 120);

        g2d.drawRect(50, 150, 100, 30);
        g2d.drawString("Play Now", 55, 170);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isNameFieldSelected) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !name.isEmpty()) {
                name = name.substring(0, name.length() - 1);
            } else {
                char keyChar = e.getKeyChar();
                if (Character.isLetter(keyChar) || Character.isDigit(keyChar)) {
                    name += keyChar;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isNameFieldSelected = isInsideRectangle(50, 120, 200, 30, e.getX(), e.getY());
        boolean playBtnPressed = isInsideRectangle(50, 150, 100, 30, e.getX(), e.getY());

        if(playBtnPressed) {
            System.out.println("Pressed play");
            panel.showInterface(new GamePlayScreen());
        } else {
            System.out.println("Nope");
        }
    }

    private boolean isInsideRectangle(int x, int y, int w, int h, int currentX, int currentY) {
        return currentX >= x && currentX < (x + w) && currentY >= y && currentY < (y + h);
    }

}
