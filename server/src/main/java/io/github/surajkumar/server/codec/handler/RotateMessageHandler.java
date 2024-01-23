package io.github.surajkumar.server.codec.handler;

import io.github.surajkumar.game.Location;
import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.codec.message.RotateMessage;

public class RotateMessageHandler implements MessageHandler<RotateMessage, RotateMessage> {

    @Override
    public RotateMessage handle(RotateMessage message) {
        Player player = message.player();
        Location current = player.getLocation();
        Location location = new Location(current.x(), current.y(), message.orientation());
        message.player().setLocation(location);
        return null;
    }
}
