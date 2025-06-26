package com.lifedestroyed.uct.commands;

import com.lifedestroyed.uct.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MuteCommand implements CommandExecutor {
    private final Main plugin;
    private final Map<UUID, Long> mutedPlayers = new HashMap<>();

    public MuteCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("uct.mute")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /mute <player> [time] [reason]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        long muteTime = args.length > 1 ? parseTime(args[1]) : -1;
        String reason = args.length > 2 ? String.join(" ", args).substring(args[0].length() + args[1].length() + 2)
                : plugin.getConfigManager().getMuteReason();

        mutedPlayers.put(target.getUniqueId(), muteTime == -1 ? Long.MAX_VALUE : System.currentTimeMillis() + muteTime);
        sender.sendMessage("§aMuted " + target.getName() + "§7. Reason: " + reason);
        target.sendMessage("§cYou've been muted. Reason: " + reason);
        return true;
    }

    private long parseTime(String timeStr) {
        try {
            if (timeStr.endsWith("s")) {
                return Long.parseLong(timeStr.substring(0, timeStr.length() - 1)) * 1000;
            } else if (timeStr.endsWith("m")) {
                return Long.parseLong(timeStr.substring(0, timeStr.length() - 1)) * 60000;
            } else if (timeStr.endsWith("h")) {
                return Long.parseLong(timeStr.substring(0, timeStr.length() - 1)) * 3600000;
            }
            return Long.parseLong(timeStr) * 1000;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isMuted(UUID playerId) {
        return mutedPlayers.containsKey(playerId) && mutedPlayers.get(playerId) > System.currentTimeMillis();
    }
}