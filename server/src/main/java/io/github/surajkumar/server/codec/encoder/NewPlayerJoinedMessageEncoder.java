package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.Opcode;
import io.github.surajkumar.server.codec.message.NewPlayerJoinedMessage;
import io.vertx.core.buffer.Buffer;

public class NewPlayerJoinedMessageEncoder implements MessageEncoder<NewPlayerJoinedMessage> {

    @Override
    public Buffer encode(NewPlayerJoinedMessage message) {
        Player player = message.player();
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(Opcode.NEW_PLAYER_JOINED);
        appendPlayerToBuffer(buffer, player);
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
