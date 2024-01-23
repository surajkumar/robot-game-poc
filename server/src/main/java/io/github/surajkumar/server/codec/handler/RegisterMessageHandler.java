package io.github.surajkumar.server.codec.handler;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.game.Location;
import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.codec.message.RegisterMessage;
import io.github.surajkumar.server.codec.message.RegisterResponseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterMessageHandler implements MessageHandler<RegisterResponseMessage, RegisterMessage> {

    @Override
    public RegisterResponseMessage handle(RegisterMessage registerMessage) {

        Player player = new Player(
                registerMessage.socket(),
                ThreadLocalRandom.current().nextInt(0, 10000),
                registerMessage.name(),
                registerMessage.color(),
                new Location(ThreadLocalRandom.current().nextInt(0, 500), ThreadLocalRandom.current().nextInt(0, 200), 0),
                10);

        List<Player> players = new ArrayList<>(Game.getInstance().getPlayers());
        Game.getInstance().addPlayer(player);

        return new RegisterResponseMessage(player, players);
    }
}
