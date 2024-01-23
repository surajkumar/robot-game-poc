package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.GameInformationMessage;
import io.vertx.core.buffer.Buffer;

import java.util.List;

public class GameInformationEncoder implements MessageEncoder<GameInformationMessage> {

    @Override
    public Buffer encode(GameInformationMessage message) {
        List<Player> players = message.players();

        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.GAME_INFORMATION);
        return null;
    }
}
