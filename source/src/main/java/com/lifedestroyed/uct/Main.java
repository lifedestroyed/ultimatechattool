package com.lifedestroyed.uct;

import com.lifedestroyed.uct.commands.MuteCommand;
import com.lifedestroyed.uct.commands.UCTCommand;
import com.lifedestroyed.uct.listeners.ChatListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private MuteCommand muteCommand;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        muteCommand = new MuteCommand(this);

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getCommand("mute").setExecutor(muteCommand);
        getCommand("uct").setExecutor(new UCTCommand(this));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MuteCommand getMuteCommand() {
        return muteCommand;
    }
}