package com.lifedestroyed.uct.listeners;

import com.lifedestroyed.uct.Main;
import com.lifedestroyed.uct.utils.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public final class ChatListener implements Listener {
    private final Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);

        if (plugin.getMuteCommand().isMuted(player.getUniqueId())) {
            player.sendMessage(ColorUtils.format("&cYou are muted!"));
            return;
        }

        String message = event.getMessage();
        boolean isGlobal = message.startsWith("!");

        if (plugin.getConfigManager().isColorsEnabled() && player.hasPermission("uct.colors")) {
            message = ColorUtils.format(isGlobal ? message.substring(1) : message);
        } else if (isGlobal) {
            message = message.substring(1);
        }

        String finalMessage = formatMessage(player, message);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isGlobal) {
                    Bukkit.broadcastMessage(finalMessage);
                } else {
                    sendLocal(player, finalMessage);
                }
            }
        }.runTask(plugin);
    }

    private void sendLocal(Player sender, String message) {
        sender.sendMessage(message);
        for (Player p : sender.getWorld().getPlayers()) {
            if (!p.equals(sender) && p.getLocation().distance(sender.getLocation()) <= plugin.getConfigManager().getLocalRadius()) {
                p.sendMessage(message);
            }
        }
    }

    private String formatMessage(Player player, String message) {
        String format = plugin.getConfigManager().getChatFormat();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(player, format);
        }

        String prefix = "";
        try {
            Chat chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
            prefix = chat.getPlayerPrefix(player) != null ? chat.getPlayerPrefix(player) : "";
        } catch (Exception ignored) {}

        return ColorUtils.format(
                format.replace("{prefix}", prefix)
                        .replace("{player}", player.getName())
                        .replace("{message}", message)
        );
    }
}