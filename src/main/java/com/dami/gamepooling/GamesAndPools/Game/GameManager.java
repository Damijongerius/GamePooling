package com.dami.gamepooling.GamesAndPools.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameManager extends BukkitRunnable implements Listener {

    Map<Integer, IGame> Games = new HashMap<>();

    //add playground (SpawnLocation, World)

    public void registerGame(IGame IGame, int id) {
        Games.put(id, IGame);
    }

    abstract void StartGame();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for(IGame game : Games.values()){
            if(game instanceof ActiveIGame){
                ActiveIGame activeGame = (ActiveIGame) game;
                activeGame.getInitialPlayers().forEach(player -> activeGame.onPlayerReJoin(e.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getPlayer());
        if(activeIGame != null) activeIGame.onPlayerQuit(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getEntity());
        if(activeIGame != null) activeIGame.onPlayerDeath(e.getEntity());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerDeathEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getEntity());
        if(activeIGame != null) activeIGame.onPlayerRespawn(e.getEntity());
    }

    @EventHandler
    public void onPlayerHitOtherPlayer(EntityDamageByEntityEvent e) {
        ActiveIGame activeIGame = getPlayerInGame((Player) e.getEntity());
        if(e.getDamager() instanceof Player){
            if(activeIGame != null) activeIGame.onPlayerHitOtherPlayer((Player) e.getDamager(), (Player) e.getEntity());
        }else{
            if(activeIGame != null) activeIGame.onPlayerGetHitByEntity(e.getDamager(), (Player) e.getEntity());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        ActiveIGame activeIGame = getPlayerInGame(e.getPlayer());
        if(activeIGame != null) activeIGame.onInteractEvent(e);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e){
        ActiveIGame activeIGame = getPlayerInGame((Player) e.getWhoClicked());
        if(activeIGame != null) activeIGame.onPlayerClick(e);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        ActiveIGame activeIGame = getPlayerInGame(e.getPlayer());
        if(activeIGame != null) activeIGame.onPlayerMove(e.getPlayer());
    }

    abstract void generateWorld(int id);

    abstract void deleteWorld(int id);

    abstract void stopGame(int id);

    List<IGame> getGames(){
        return (List<IGame>) Games.values();
    }

    private ActiveIGame getPlayerInGame(Player player) {
        for (IGame game : Games.values()) {
            if (game.getPlayers().contains(player.getUniqueId())) {
                if(game instanceof ActiveIGame){
                    return (ActiveIGame) game;
                }
            }
        }
        return null;
    }

}
