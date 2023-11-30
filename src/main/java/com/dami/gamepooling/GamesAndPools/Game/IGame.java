package com.dami.gamepooling.GamesAndPools.Game;

import org.bukkit.World;

import java.util.List;
import java.util.UUID;

public interface IGame {

    ActiveIGame startGame();

    long getStartTime();

    World getWorld();

    List<UUID> getPlayers();

    void addPlayer(UUID player);

    void removePlayer(UUID player);
}
