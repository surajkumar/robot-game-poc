package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.PlayerDeathMessage;
import io.vertx.core.buffer.Buffer;

public class PlayerDeathMessageEncoder implements MessageEncoder<PlayerDeathMessage> {

    @Override
    public Buffer encode(PlayerDeathMessage message) {
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.PLAYER_DEATH);
        buffer.appendInt(message.player().getId());
        return buffer;
    }
}
