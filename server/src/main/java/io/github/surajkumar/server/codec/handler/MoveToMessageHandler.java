package io.github.surajkumar.server.codec.handler;

import io.github.surajkumar.game.DummyPathFinder;
import io.github.surajkumar.game.Location;
import io.github.surajkumar.server.codec.message.MoveToMessage;

import java.util.List;

public class MoveToMessageHandler implements MessageHandler<MoveToMessage, MoveToMessage> {

    @Override
    public MoveToMessage handle(MoveToMessage message) {
        List<Location> nodes = DummyPathFinder.pathTo(
                message
                        .player()
                        .getLastLocation(),
                message
                        .location());

        message.player().walk(nodes);

        return null;
    }
}
