package com.dami.gamepooling.GamesAndPools.Game;

import com.dami.gamepooling.GamePooling;
import com.dami.gamepooling.GamesAndPools.Pool.IPool;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GameManager extends BukkitRunnable implements Listener {

    private MultiverseCore multiverseCore;

    protected Map<Integer, IGame> games = new HashMap<>();

    protected final List<String> maps;

    protected GameManager(List<String> maps) {
        this.maps = maps;

        AtomicInteger id = new AtomicInteger();
        maps.forEach((name) -> {
            for (int i = 0; i < 2; i++) {
                registerGame(name, id.get());
                id.getAndIncrement();
            }
        });

        multiverseCore = GamePooling.getInstance().getMultiverseCore();
    }

    public abstract void registerGame(String playground, int id);

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

    public boolean PoolToGame(IPool pool){
        AtomicBoolean done = new AtomicBoolean(false);
        games.forEach((id,game) ->{
            if(game.getClass() ==  pool.gameClass()){
                if(game.getGameState() == GameState.WAITING){
                    game.setPlayers(pool.getPLayers());
                    games.remove(id);

                    games.put(id,game.startGame());
                    done.set(true);
                    game.setGameState(GameState.RUNNING);
                }
            }
        });

        return done.get();
    }

    public void endGame(ActiveIGame endingGame) {
        games.forEach((id, game) -> {
            if (game == endingGame) {
                endingGame.CleanGame();

                games.replace(id, endingGame.resetGame());
            }
        });
    }

    protected void cloneWorld(String worldName, int id){
        multiverseCore.cloneWorld(worldName, worldName + id, "VoidGen");
    }

    protected boolean worldExists(String worldName){
        return multiverseCore.getMVWorldManager().isMVWorld(worldName);
    }

    protected void deleteWorld(String worldName){
        multiverseCore.getMVWorldManager().deleteWorld(worldName);
    }

    public List<IGame> getGames(){
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

    @Override
    public void run(){
        games.forEach((id,game) ->{
            if(game instanceof ActiveIGame){
                ((ActiveIGame) game).update();
            }
        });
    }

}
