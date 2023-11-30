package com.dami.gamepooling;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.plugin.java.JavaPlugin;

public final class GamePooling extends JavaPlugin {
    private MultiverseCore multiverseCore;

    @Override
    public void onEnable() {
        this.multiverseCore = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (multiverseCore == null) {
            getLogger().severe("Multiverse-Core not found! Make sure it's installed.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
