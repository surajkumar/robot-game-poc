package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Player;

import java.util.List;

public record RegisterResponseMessage(Player player, List<Player> players) implements Message {
}
