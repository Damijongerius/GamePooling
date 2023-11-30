package com.dami.gamepooling.GamesAndPools.Pool;

import com.dami.gamepooling.GamePooling;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PoolManager extends BukkitRunnable {

    public static Map<String, IPool> poolTypes = new HashMap<>();

    private List<IPool> IPools = new ArrayList<>();

    private List<UUID> players = new ArrayList<>();

    public PoolManager(GamePooling gamePooling) {
        runTaskTimerAsynchronously(gamePooling, 20, 20);
    }

    public void onPlayerJoinPool(UUID player, String poolType) {
        if(players.contains(player)) return;

        if(!poolTypes.containsKey(poolType)) return;

        players.add(player);

        for (IPool IPool : IPools) {
            boolean stop = false;

            if(IPool.getPLayers().size() < IPool.getMaxPlayers()) {
                IPool.addPlayer(player);
                stop = true;
            }

            if (!stop) continue;

            if(IPool.getPLayers().size() == IPool.getMaxPlayers()) {
                IPool newIPool = poolTypes.get(poolType);
                newIPool.addPlayer(player);
                IPools.add(newIPool);
            }

            if(IPool.getPLayers().size() >= IPool.getMinPlayers()) {
                IPool.setStartDelay(IPool.getMinPlayerDelay());
            }

            break;
        }
    }

    public void onPlayerLeavePool(UUID player) {
        if(!players.contains(player)) return;

        players.remove(player);

        for (IPool IPool : IPools) {
            boolean stop = false;
            if(IPool.getPLayers().contains(player)) {
                IPool.removePlayer(player);
                stop = true;
            }

            if(!stop) continue;

            if(IPool.getPLayers().size() <= IPool.getMinPlayers()) {
                IPool.setStartDelay(-1);
            }

            break;
        }

    }

    @Override
    public void run() {

    }
}
