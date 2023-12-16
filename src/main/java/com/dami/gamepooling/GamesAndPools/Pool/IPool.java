package com.dami.gamepooling.GamesAndPools.Pool;

import com.dami.gamepooling.GamesAndPools.Game.IGame;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public interface IPool {

    List<UUID> getPLayers();

    void addPlayer(UUID player);

    void removePlayer(UUID player);

    Class<? extends IGame> gameClass();

    int getStartDelay();

    int getMaxPlayers();

    int getMinPlayers();

    int getMinPlayerDelay();

    void setStartDelay(int delay);


}
