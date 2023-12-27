package com.dami.gamepooling.GamesAndPools.Pool;

import com.dami.gamepooling.GamePooling;
import com.dami.gamepooling.GamesAndPools.Game.GameManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PoolManager extends BukkitRunnable{

    private final Map<String, IPool> poolTypes;

    private final List<IPool> pools = new ArrayList<>();

    private final List<UUID> players = new ArrayList<>();

    private final GameManager gameManager;

    public PoolManager(JavaPlugin gamePooling, GameManager gameManager, Map<String, IPool> poolTypes) {
        runTaskTimerAsynchronously(gamePooling, 20, 20);
        this.gameManager = gameManager;
        this.poolTypes = poolTypes;
    }

    public void onPlayerJoinPool(UUID player, String poolType) {
        System.out.println("player joining pool");
        if (players.contains(player)) {
            Bukkit.getPlayer(player).sendMessage("already in queue");
            return;
        }
        System.out.println("player not in queue");
        if (!poolTypes.containsKey(poolType)) {
            Bukkit.getPlayer(player).sendMessage("pool type not found");
            return;
        }
        System.out.println("pool type found");
        List<IPool> possiblePools = new ArrayList<>();
        pools.forEach((pool) -> {
            if (pool.getClass() == poolTypes.get(poolType).getClass()) {
                possiblePools.add(pool);
            }
        });
        System.out.println("possible pools found");

        if(possiblePools.isEmpty()){
            System.out.println("possible pools empty");
            IPool newIPool = poolTypes.get(poolType);
            newIPool.addPlayer(player);
            Bukkit.getPlayer(player).sendMessage("joined queue");
            pools.add(newIPool);
            if (newIPool.getPLayers().size() == newIPool.getMinPlayers()) {
                newIPool.setStartDelay(newIPool.getMinPlayerDelay());
            }
            return;
        }

        possiblePools.forEach((pool) -> {
            System.out.println(pool.getClass().getSimpleName());
            if (pool.getPLayers().size() < pool.getMaxPlayers()) {

                Bukkit.getPlayer(player).sendMessage("joined queue");
                pool.addPlayer(player);
                if (pool.getPLayers().size() == pool.getMinPlayers()) {
                    pool.setStartDelay(pool.getMinPlayerDelay());
                }
                return;

            }else if(pool.getPLayers().size() == pool.getMaxPlayers()) {

                IPool newIPool = poolTypes.get(poolType);
                newIPool.addPlayer(player);
                Bukkit.getPlayer(player).sendMessage("joined queue");
                pools.add(newIPool);
                if (pool.getPLayers().size() == pool.getMinPlayers()) {
                    pool.setStartDelay(pool.getMinPlayerDelay());
                }
                return;
            }
        });
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
        if(pools.isEmpty()) return;

        Iterator<IPool> iterator = pools.iterator();
        while (iterator.hasNext()) {
            IPool pool = iterator.next();
            if(pool.getStartDelay() == -1){
                pool.getPLayers().forEach((player) -> {
                    Bukkit.getPlayer(player).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("WaitingForPlayers..."));
                });
                continue;
            }
            if(pool.getStartDelay() > 0) {
                pool.setStartDelay(pool.getStartDelay() -1);
                pool.getPLayers().forEach((player) -> {
                    Bukkit.getPlayer(player).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Starting in " + pool.getStartDelay()));
                });
            }else {
                if (gameManager.PoolToGame(pool)) {
                    iterator.remove();
                }
            }
        }
    }
}
