package io.github.surajkumar.server.codec.decoder;

import io.github.surajkumar.server.codec.message.RegisterMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.awt.*;

public class RegisterMessageDecoder implements MessageDecoder<RegisterMessage> {

    @Override
    public RegisterMessage decode(NetSocket socket, Buffer buffer) {
        int rgb = buffer.getInt(4);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        Color color = new Color(red, green, blue);
        String name = buffer.getString(8, buffer.length());

        return new RegisterMessage(socket, name, color);
    }
}
