package com.dami.gamepooling;

import com.dami.gamepooling.Commands.QueueCommand;
import com.dami.gamepooling.GamesAndPools.Game.GameManager;
import com.dami.gamepooling.GamesAndPools.Pool.IPool;
import com.dami.gamepooling.GamesAndPools.Pool.PoolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

public final class GamePooling extends JavaPlugin {

    private static GamePooling instance;
    private MultiverseCore multiverseCore;

    private GameManager gameManager;

    private PoolManager poolManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getMultiverseCore();

        new QueueCommand(this,poolManager);
    }

    private void getMultiverseCore(){
        this.multiverseCore = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (multiverseCore == null) {
            getLogger().severe("Multiverse-Core not found! Make sure it's installed.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void registerGameManager(GameManager manager, Map<String, IPool> poolMap){

        if(this.gameManager != null){
            Bukkit.getLogger().log(Level.INFO,"game manager is already registered ");
            return;
        }

        this.gameManager = manager;
        gameManager.runTaskTimerAsynchronously(this, 0, 0);

        poolManager = new PoolManager(this, gameManager, poolMap);

    }

    public GameManager getGameManager(){
        return gameManager;
    }

    public static GamePooling getInstance(){
        return instance;
    }
}
