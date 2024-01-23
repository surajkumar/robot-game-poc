package io.github.surajkumar.server.codec.decoder;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.server.codec.message.ShootBulletMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class ShootBulletMessageDecoder implements MessageDecoder<ShootBulletMessage> {
    @Override
    public ShootBulletMessage decode(NetSocket socket, Buffer buffer) {
        return new ShootBulletMessage(Game.getInstance().getPlayerBySocket(socket).orElse(null));
    }
}
