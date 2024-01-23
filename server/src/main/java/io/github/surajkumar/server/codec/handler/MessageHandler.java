package io.github.surajkumar.server.codec.handler;

import io.github.surajkumar.server.codec.message.Message;

public interface MessageHandler<T extends Message, V extends Message> {
    T handle(V message);
}
