package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.GameOverMessage;
import io.vertx.core.buffer.Buffer;

public class SendGameOverMessageEncoder implements MessageEncoder<GameOverMessage> {
    @Override
    public Buffer encode(GameOverMessage message) {
        return Buffer.buffer().appendInt(Opcode.GAME_OVER);
    }
}
