package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Location;
import io.github.surajkumar.game.Player;

public record MoveToMessage(Player player, Location location) implements Message {
}
