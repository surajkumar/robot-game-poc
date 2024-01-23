package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Player;

public record NewPlayerJoinedMessage(Player player) implements Message {
}
