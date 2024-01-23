package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.RegisterResponseMessage;
import io.vertx.core.buffer.Buffer;

import java.util.List;

public class RegisterResponseMessageEncoder implements MessageEncoder<RegisterResponseMessage> {

    @Override
    public Buffer encode(RegisterResponseMessage registerResponseMessage) {
        List<Player> players = registerResponseMessage.players();
        Player player = registerResponseMessage.player();
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.REGISTER);
        buffer.appendInt(players.size() + 1);
        appendPlayerToBuffer(buffer, player);
        for (Player p : players) {
            appendPlayerToBuffer(buffer, p);
        }
        return buffer;
    }

    private void appendPlayerToBuffer(Buffer buffer, Player p) {
        int color = (p.getColor().getRed() << 16) | (p.getColor().getGreen() << 8) | p.getColor().getBlue();
        buffer.appendInt(color);
        buffer.appendInt(p.getLocation().x());
        buffer.appendInt(p.getLocation().y());
        buffer.appendInt(p.getHealth());
        buffer.appendInt(p.getId());
        buffer.appendDouble(p.getLocation().orientation());
        buffer.appendInt(p.getName().length());
        buffer.appendString(p.getName());
    }
}
