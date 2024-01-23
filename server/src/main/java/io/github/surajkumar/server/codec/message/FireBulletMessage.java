package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Bullet;
import io.github.surajkumar.game.Player;

public record FireBulletMessage(Player player, Bullet bullet) implements Message {
}
