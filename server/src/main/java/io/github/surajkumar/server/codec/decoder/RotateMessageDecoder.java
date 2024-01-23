package io.github.surajkumar.server.codec.decoder;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.server.codec.message.RotateMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class RotateMessageDecoder implements MessageDecoder<RotateMessage> {

    @Override
    public RotateMessage decode(NetSocket socket, Buffer buffer) {
        return new RotateMessage(Game.getInstance().getPlayerBySocket(socket).get(), buffer.getDouble(4));
    }
}
