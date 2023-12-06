package com.dami.gamepooling.Commands;

import com.dami.gamepooling.GamesAndPools.Pool.PoolManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class QueueCommand implements TabExecutor {

    private final PoolManager poolManager;

    public QueueCommand(JavaPlugin plugin, PoolManager poolManager){
        this.poolManager = poolManager;
        plugin.getCommand("queue").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("gamepooling.que")) {
        } else {
            sender.sendMessage("You do not have permission!");
        }

        Player player = (Player) sender;

        if(args.length == 0){
            help(player);
        }

        switch (args[0]) {
            case "join" -> {
                if(args.length == 2){
                    joinQueue(player,args[1]);
                    break;
                }
                //join random
            }
            case "leave" -> leaveQueue(player);
            case "info" -> {
                if(args.length == 2){
                    queueInfo(player,args[1]);
                    break;
                }
                info(player);
            }
            default -> help(player);
        }

        return true;
    }

    private void joinQueue(Player player, String name) {
        //join queue
        poolManager.onPlayerJoinPool(player.getUniqueId(),name);
    }

    private void joinQueue(Player player){
        List<String> pooltypes = poolManager.getPoolTypes();
        Random rnd = new Random(pooltypes.size());
        joinQueue(player,pooltypes.get(rnd.nextInt()));
    }

    private void leaveQueue(Player player) {
        poolManager.onPlayerLeavePool(player.getUniqueId());
    }

    private void info(Player player){
        player.sendMessage(" kuch kuch dami is dit vergeten te veranderen");
    }

    private void queueInfo(Player player, String name){
        player.sendMessage("dit ook");
    }

    private void help(Player player){
        player.sendMessage(" doe niet achterlijk en snap het gewoon");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
