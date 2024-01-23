package io.github.surajkumar.server.codec.encoder;

import io.github.surajkumar.server.codec.message.Message;
import io.vertx.core.buffer.Buffer;

public interface MessageEncoder<T extends Message> {
    Buffer encode(T message);
}
