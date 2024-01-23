package io.github.surajkumar.server;

import io.github.surajkumar.server.codec.encoder.MessageEncoder;
import io.github.surajkumar.server.codec.message.Message;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.List;

public class MessageSender {


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void sendMessage(NetSocket socket, Message message) {
        MessageEncoder encoder = TcpHandler.getMessageEncoder(message.getClass());
        if(encoder == null) {
            throw new NullPointerException("Encoder for " + message.getClass() + " is null");
        }
        Buffer buffer = encoder.encode(message);
        socket.write(buffer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void sendMessages(NetSocket socket, List<? extends Message> messages) {
        for(Message message : messages) {
            MessageEncoder encoder = TcpHandler.getMessageEncoder(message.getClass());
            if (encoder == null) {
                throw new NullPointerException("Encoder for " + message.getClass() + " is null");
            }
            Buffer buffer = encoder.encode(message);
            socket.write(buffer);
        }
    }
}
