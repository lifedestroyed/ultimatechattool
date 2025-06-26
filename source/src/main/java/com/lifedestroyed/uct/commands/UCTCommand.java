package com.lifedestroyed.uct.commands;

import com.lifedestroyed.uct.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class UCTCommand implements CommandExecutor {
    private final Main plugin;

    public UCTCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6UCT Commands:");
            sender.sendMessage("§e/uct reload §7- Reload config");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("uct.admin")) {
            plugin.getConfigManager().reload();
            sender.sendMessage("§aConfig reloaded!");
            return true;
        }

        sender.sendMessage("§cUnknown command.");
        return true;
    }
}