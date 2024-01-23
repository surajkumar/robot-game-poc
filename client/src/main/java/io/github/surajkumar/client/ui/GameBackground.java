package io.github.surajkumar.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GameBackground {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameBackground.class);
    private static final BufferedImage BACKGROUND_IMAGE;
    private static final ImageIcon LAUNCH_BG;
    static {
        BACKGROUND_IMAGE = loadImage("/game_background.png");
        LAUNCH_BG = loadGifImage("/launch_bg.gif");
    }

    private static BufferedImage loadImage(String path) {
        BufferedImage bufferedImage;
        try {
            InputStream input = GameBackground.class.getResourceAsStream(path);
            if(input != null) {
                bufferedImage = ImageIO.read(input);
            } else {
                bufferedImage = null;
            }
        } catch (IOException e) {
            bufferedImage = null;
            LOGGER.error("Failed to load image", e);
        }
        return bufferedImage;
    }

    private static ImageIcon loadGifImage(String path) {
        URL input = GameBackground.class.getResource(path);
        if(input != null) {
            return new ImageIcon(input);
        }
        return null;
    }

    public static BufferedImage getBackgroundImage() {
        return BACKGROUND_IMAGE;
    }

    public static ImageIcon getLaunchBg() {
        return LAUNCH_BG;
    }
}
