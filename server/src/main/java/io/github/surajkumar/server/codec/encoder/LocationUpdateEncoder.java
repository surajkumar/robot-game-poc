package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.game.Location;
import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.LocationUpdateMessage;
import io.vertx.core.buffer.Buffer;

public class LocationUpdateEncoder implements MessageEncoder<LocationUpdateMessage> {

    @Override
    public Buffer encode(LocationUpdateMessage message) {
        Player player = message.player();
        Location location = message.player().getLocation();
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.LOCATION_UPDATE);
        buffer.appendInt(player.getId());
        buffer.appendInt(location.x());
        buffer.appendInt(location.y());
        buffer.appendDouble(location.orientation());
        buffer.appendInt(player.getHealth()); //shh...
        return buffer;
    }
}
