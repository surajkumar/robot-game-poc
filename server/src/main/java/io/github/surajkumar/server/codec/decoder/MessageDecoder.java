package io.github.surajkumar.server.codec.decoder;

import io.github.surajkumar.server.codec.message.Message;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public interface MessageDecoder<T extends Message> {
    T decode(NetSocket socket, Buffer buffer);
}
