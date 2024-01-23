package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Player;

public record RotateMessage(Player player, double orientation) implements Message{
}
