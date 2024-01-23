package io.github.surajkumar.server.codec.message;

import io.github.surajkumar.game.Player;

import java.util.List;

public record GameInformationMessage(List<Player> players) implements Message { }
