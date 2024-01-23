package io.github.surajkumar.server.codec.handler;

import io.github.surajkumar.game.Player;
import io.github.surajkumar.server.codec.message.ShootBulletMessage;

public class ShootBulletMessageHandler implements MessageHandler<ShootBulletMessage, ShootBulletMessage> {
    @Override
    public ShootBulletMessage handle(ShootBulletMessage message) {
        Player player = message.player();
        player.getWeapon().shoot(player.getId(), player.getLocation());
        return null;
    }
}
