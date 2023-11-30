package com.dami.gamepooling.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QueCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("gamepooling.que")) {
        } else {
            sender.sendMessage("You do not have permission!");
        }

        Player player = (Player) sender;

        switch (args[0]) {
            case "join":
                break;
            case "leave":
                break;
            case "info":
                break;
            case "help":
                break;
            default:
                break;
        }

        return true;
    }

    private void joinQue(Player player) {

    }

    private void leaveQue(Player player) {

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
