package com.dami.gamepooling.GamesAndPools.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameManager extends BukkitRunnable implements Listener {

    Map<Integer, IGame> games = new HashMap<>();

    final Map<String, PlayGround> maps;

    private GameManager(Map<String,PlayGround> maps){
        this.maps = maps;
    }

    public void registerGame(IGame IGame, int id) {
        games.put(id, IGame);
    }

    abstract void StartGame();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for(IGame game : games.values()){
            if(game instanceof ActiveIGame){
                ActiveIGame activeGame = (ActiveIGame) game;
                activeGame.getInitialPlayers().forEach(player -> activeGame.onPlayerReJoin(e.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getPlayer());
        activeIGame.onPlayerQuit(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getEntity());
        activeIGame.onPlayerDeath(e.getEntity());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerDeathEvent e) {
        ActiveIGame activeIGame = getPlayerInGame(e.getEntity());
        activeIGame.onPlayerRespawn(e.getEntity());
    }

    @EventHandler
    public void onPlayerHitOtherPlayer(EntityDamageByEntityEvent e) {
        ActiveIGame activeIGame = getPlayerInGame((Player) e.getEntity());
        if(e.getDamager() instanceof Player){
            activeIGame.onPlayerHitOtherPlayer((Player) e.getDamager(), (Player) e.getEntity());
        }
    }

    abstract void generateWorld(int id);

    abstract void deleteWorld(int id);

    abstract void stopGame(int id);

    List<IGame> getGames(){
        return (List<IGame>) games.values();
    }

    private ActiveIGame getPlayerInGame(Player player) {
        for (IGame game : games.values()) {
            if (game.getPlayers().contains(player.getUniqueId())) {
                if(game instanceof ActiveIGame){
                    return (ActiveIGame) game;
                }
            }
        }
        return null;
    }

}
