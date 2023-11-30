package com.dami.gamepooling.GamesAndPools.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public abstract class ActiveIGame implements IGame {

    private final List<UUID> initialPlayers;

    protected ActiveIGame(List<UUID> initialPlayers) {
        this.initialPlayers = initialPlayers;
    }

    public List<UUID> getInitialPlayers() {
        return initialPlayers;
    }

    public abstract void update();

    public abstract void onPlayerQuit(Player player);

    public abstract void onPlayerReJoin(Player player);

    public abstract void onPlayerDeath(Player player);

    public abstract void onPlayerRespawn(Player player);

    public abstract void onInteractEvent(PlayerInteractEvent event);

    public abstract void onPlayerClick(InventoryClickEvent event);

    public abstract void onPlayerHitOtherPlayer(Player hitter, Player hit);
}
