package io.github.surajkumar.server;

import io.github.surajkumar.game.Game;
import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.codec.decoder.*;
import io.github.surajkumar.server.codec.encoder.*;
import io.github.surajkumar.server.codec.handler.*;
import io.github.surajkumar.server.codec.message.*;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TcpHandler implements Handler<NetSocket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpHandler.class);
    private static final Map<Integer, MessageDecoder<?>> MESSAGE_DECODERS = new HashMap<>();
    private static final Map<Class<?>, MessageHandler<?, ?>> MESSAGE_HANDLERS = new HashMap<>();
    private static final Map<Class<?>, MessageEncoder<?>> MESSAGE_ENCODERS = new HashMap<>();

    static {
        MESSAGE_DECODERS.put(Opcode.REGISTER, new RegisterMessageDecoder());
        MESSAGE_HANDLERS.put(RegisterMessage.class, new RegisterMessageHandler());
        MESSAGE_ENCODERS.put(RegisterResponseMessage.class, new RegisterResponseMessageEncoder());

        MESSAGE_DECODERS.put(Opcode.FIRE_BULLET, new ShootBulletMessageDecoder());
        MESSAGE_HANDLERS.put(ShootBulletMessage.class, new ShootBulletMessageHandler());
        MESSAGE_ENCODERS.put(FireBulletMessage.class, new FireBulletMessageEncoder());

        MESSAGE_DECODERS.put(Opcode.MOVE_TO, new MoveToMessageDecoder());
        MESSAGE_HANDLERS.put(MoveToMessage.class, new MoveToMessageHandler());

        MESSAGE_DECODERS.put(Opcode.ROTATE_PLAYER, new RotateMessageDecoder());
        MESSAGE_HANDLERS.put(RotateMessage.class, new RotateMessageHandler());

        MESSAGE_ENCODERS.put(LocationUpdateMessage.class, new LocationUpdateEncoder());
        MESSAGE_ENCODERS.put(NewPlayerJoinedMessage.class, new NewPlayerJoinedMessageEncoder());
        MESSAGE_ENCODERS.put(PlayerDeathMessage.class, new PlayerDeathMessageEncoder());
        MESSAGE_ENCODERS.put(GameOverMessage.class, new SendGameOverMessageEncoder());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void handle(NetSocket socket) {
        socket.handler(buffer -> {
            int opcode = buffer.getInt(0);

            LOGGER.info("Received opcode {}", opcode);

            MessageDecoder<?> decoder = MESSAGE_DECODERS.get(opcode);
            if(decoder == null) {
                LOGGER.warn("No decoder found for opcode {}", opcode);
                return;
            }

            Message decoded = decoder.decode(socket, buffer);
            if(decoded == null) {
                LOGGER.warn(decoder.getClass() + " decoded to a null message");
                return;
            }

            MessageHandler handler = MESSAGE_HANDLERS.get(decoded.getClass());
            if(handler == null) {
                LOGGER.warn("No handler found for {}", decoded.getClass());
                return;
            }

            Message message = handler.handle(decoded);
            if(message != null) {
                MessageEncoder encoder = MESSAGE_ENCODERS.get(message.getClass());
                if(encoder == null) {
                    LOGGER.warn("No encoder found for {}", message.getClass());
                    return;
                }
                Buffer response = encoder.encode(message);
                if(response != null) {
                    socket.write(response);
                }
            }
        });

        socket.closeHandler(v -> {
            Optional<Player> player = Game.getInstance().getPlayerBySocket(socket);

            player.ifPresent(p -> Game.getInstance().removePlayer(p));

            LOGGER.info("{} has disconnected.", socket);
        });
    }

    public static MessageEncoder<?> getMessageEncoder(Class<?> clazz) {
        return MESSAGE_ENCODERS.get(clazz);
    }
}
