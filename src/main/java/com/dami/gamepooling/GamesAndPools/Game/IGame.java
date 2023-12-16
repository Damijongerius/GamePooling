package com.dami.gamepooling.GamesAndPools.Game;

import org.bukkit.World;

import java.util.List;
import java.util.UUID;

public interface IGame {

    ActiveIGame startGame();

    GameState getGameState();

    GameState setGameState(GameState gameState);

    long getStartTime();

    World getWorld();

    List<UUID> getPlayers();

    void setPlayers(List<UUID> players);
}
