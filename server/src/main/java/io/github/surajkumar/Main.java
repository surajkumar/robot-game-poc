package io.github.surajkumar;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.server.TcpServer;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.info("Starting server");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TcpServer(43594), res -> {
            if (res.succeeded()) {
                LOGGER.info("Server has been deployed successfully.");
            } else {
                LOGGER.error("Server has failed to launch:" + res.cause().getMessage());
                System.exit(1);
            }
        });

        while(true) {
            Game.getInstance().run();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
