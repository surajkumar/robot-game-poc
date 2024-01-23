package io.github.surajkumar.server.codec.message;

import io.vertx.core.net.NetSocket;

import java.awt.*;

public record RegisterMessage(NetSocket socket, String name, Color color) implements Message {
}
