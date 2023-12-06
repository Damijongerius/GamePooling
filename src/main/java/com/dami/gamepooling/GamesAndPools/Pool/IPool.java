package com.dami.gamepooling.GamesAndPools.Pool;

import com.dami.gamepooling.GamesAndPools.Game.IGame;

import java.util.List;
import java.util.UUID;

public interface IPool {

    List<UUID> getPLayers();

    List<UUID> addPlayer(UUID player);

    List<UUID> removePlayer(UUID player);

    <T extends IGame> T gameClass();

    int getStartDelay();

    int getMaxPlayers();

    int getMinPlayers();

    int getMinPlayerDelay();

    void setStartDelay(int delay);


}
