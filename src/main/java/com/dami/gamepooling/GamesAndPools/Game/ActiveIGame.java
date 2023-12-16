package com.dami.gamepooling.GamesAndPools.Game;

import com.dami.gamepooling.GamePooling;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

    public abstract void onPlayerHitOtherPlayer(Player hitter, Player hit);

    public abstract void onPlayerGetHitByEntity(Entity hitter, Player hit);

    public abstract void CleanGame();

    public abstract IGame resetGame();

    public void endSession(){
        GamePooling.getInstance().getGameManager().endGame(this);
    }
}
