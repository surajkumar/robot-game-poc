package io.github.surajkumar.server.codec.decoder;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.game.Location;
import io.github.surajkumar.server.codec.message.MoveToMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class MoveToMessageDecoder implements MessageDecoder<MoveToMessage> {

    @Override
    public MoveToMessage decode(NetSocket socket, Buffer buffer) {
        int x = buffer.getInt(4);
        int y = buffer.getInt(8);
        double orientation = buffer.getDouble(12);

        return Game
                .getInstance()
                .getPlayerBySocket(socket)
                .map(value -> new MoveToMessage(value, new Location(x, y, orientation)))
                .orElse(null);
    }
}
