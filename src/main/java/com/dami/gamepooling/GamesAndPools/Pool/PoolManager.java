package com.dami.gamepooling.GamesAndPools.Pool;

import com.dami.gamepooling.GamePooling;
import com.dami.gamepooling.GamesAndPools.Game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PoolManager extends BukkitRunnable{

    private Map<String, IPool> poolTypes;

    private final List<IPool> pools = new ArrayList<>();

    private final List<UUID> players = new ArrayList<>();

    private final GameManager gameManager;

    public PoolManager(JavaPlugin gamePooling, GameManager gameManager, Map<String, IPool> poolTypes) {
        runTaskTimerAsynchronously(gamePooling, 20, 20);
        this.gameManager = gameManager;
        this.poolTypes = poolTypes;
    }

    public void onPlayerJoinPool(UUID player, String poolType) {
        if(players.contains(player)) return;

        if(!poolTypes.containsKey(poolType)) return;

        players.add(player);

        List<IPool> tempPools = new ArrayList<>();
        pools.forEach((pool) -> {
            if(pool.getClass() == poolTypes.get(poolType).getClass()){
                tempPools.add(pool);
            }
        });

        for (IPool pool : tempPools) {
            boolean stop = false;

            if(pool.getPLayers().size() < pool.getMaxPlayers()) {
                pool.addPlayer(player);
                stop = true;
            }

            if (!stop) continue;

            if(pool.getPLayers().size() == pool.getMaxPlayers()) {
                IPool newIPool = poolTypes.get(poolType);
                newIPool.addPlayer(player);
                pools.add(newIPool);
            }

            if(pool.getPLayers().size() == pool.getMinPlayers()) {
                pool.setStartDelay(pool.getMinPlayerDelay());
            }

            break;
        }
    }

    public void onPlayerLeavePool(UUID player) {
        if(!players.contains(player)) return;

        players.remove(player);

        for (IPool IPool : pools) {
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

    public List<String> getPoolTypes(){
        return poolTypes.keySet().stream().toList();
    }


    @Override
    public void run() {
        pools.forEach((pool) -> {
            if(pool.getStartDelay() != 0) {
                pool.setStartDelay(pool.getStartDelay() -1);
            }else{
                if(gameManager.PoolToGame(pool)){
                    pools.remove(pool);
                    return;
                }
            }
        });
    }
}
