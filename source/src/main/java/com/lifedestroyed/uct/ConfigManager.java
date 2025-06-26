package com.lifedestroyed.uct;

import com.lifedestroyed.uct.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getChatFormat() {
        return ColorUtils.format(config.getString("chat.format", "{prefix}{player}&7: {message}"));
    }

    public int getLocalRadius() {
        return config.getInt("chat.local-radius", 100);
    }

    public boolean isColorsEnabled() {
        return config.getBoolean("chat.colors-enabled", true);
    }

    public String getMuteReason() {
        return ColorUtils.format(config.getString("mute.default-reason", "&cChat violation"));
    }
}