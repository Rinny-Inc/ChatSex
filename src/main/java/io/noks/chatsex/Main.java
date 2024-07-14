package io.noks.chatsex;

import org.bukkit.plugin.java.JavaPlugin;

import io.noks.chatsex.commands.ConfigReloadCommand;
import io.noks.chatsex.listeners.BlockListener;
import io.noks.chatsex.listeners.ChatListener;
import io.noks.chatsex.listeners.PermissionsExListener;
import io.noks.chatsex.listeners.PlayerListener;
import io.noks.chatsex.manager.ConfigManager;
import io.noks.chatsex.manager.PlayerManager;

public class Main extends JavaPlugin {
	public static Main instance;
	private ConfigManager configManager;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		this.configManager = new ConfigManager(this);
		this.registerListeners();
		this.registerCommands();
	}
	
	@Override
	public void onDisable() {
		PlayerManager.players.clear();
	}
	
	private void registerListeners() {
		new PlayerListener(this);
		new ChatListener(this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		new PermissionsExListener(this);
	}
	
	private void registerCommands() {
		new ConfigReloadCommand(this);
	}
	
	public ConfigManager getConfigManager() {
		return this.configManager;
	}
	
	public void reloadConfigManager() {
		this.saveConfig();
		this.reloadConfig();
		this.configManager = new ConfigManager(this);
	}
}
