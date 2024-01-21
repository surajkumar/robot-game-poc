package game;

import java.awt.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    private static final Executor EXECUTOR_SERVICE = Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) {
        ApplicationFrame application = new ApplicationFrame();
        GamePanel gamePanel = application.getGamePanel();

        GameRobot red = new GameRobot(Color.RED, "Red");
        GameRobot blue = new GameRobot(Color.BLUE, "Blue");
        GameRobot green = new GameRobot(Color.GREEN, "Green");
        GameRobot orange = new GameRobot(Color.ORANGE, "Orange");
        GameRobot cyan = new GameRobot(Color.CYAN, "Cyan");

        GameRobot lightGrey = new GameRobot(Color.LIGHT_GRAY, "Light Grey");
        GameRobot magenta = new GameRobot(Color.MAGENTA, "Magenta");
        GameRobot yellow = new GameRobot(Color.YELLOW, "Yellow");

        gamePanel.addRobot(red);
        gamePanel.addRobot(blue);
        gamePanel.addRobot(green);
        gamePanel.addRobot(orange);
        gamePanel.addRobot(cyan);

        gamePanel.addRobot(lightGrey);
        gamePanel.addRobot(magenta);
        gamePanel.addRobot(yellow);

        application.show();

        EXECUTOR_SERVICE.execute(red);
        EXECUTOR_SERVICE.execute(blue);
        EXECUTOR_SERVICE.execute(green);
        EXECUTOR_SERVICE.execute(orange);
        EXECUTOR_SERVICE.execute(cyan);
        EXECUTOR_SERVICE.execute(lightGrey);
        EXECUTOR_SERVICE.execute(magenta);
        EXECUTOR_SERVICE.execute(yellow);
    }
}
