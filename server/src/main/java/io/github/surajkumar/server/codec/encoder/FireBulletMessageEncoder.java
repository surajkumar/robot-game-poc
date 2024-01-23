package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.game.Bullet;
import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.FireBulletMessage;
import io.vertx.core.buffer.Buffer;

public class FireBulletMessageEncoder implements MessageEncoder<FireBulletMessage> {

    @Override
    public Buffer encode(FireBulletMessage message) {
        Player player = message.player();
        Bullet bullet = message.bullet();

        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.FIRE_BULLET);
        buffer.appendInt(player.getId());
        buffer.appendInt(1);


        buffer.appendInt(bullet.getX());
        buffer.appendInt(bullet.getY());
        buffer.appendInt(bullet.getDx());
        buffer.appendInt(bullet.getDy());

        return buffer;
    }
}
