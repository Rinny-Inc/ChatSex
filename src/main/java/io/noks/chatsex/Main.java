package io.noks.chatsex;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import io.noks.chatsex.commands.ConfigReloadCommand;
import io.noks.chatsex.listeners.BlockListener;
import io.noks.chatsex.listeners.ChatListener;
import io.noks.chatsex.listeners.PermissionsExListener;
import io.noks.chatsex.listeners.PlayerListener;
import io.noks.chatsex.manager.ConfigManager;
import io.noks.chatsex.manager.PlayerManager;
import io.noks.chatsex.utils.WebUtil;

public class Main extends JavaPlugin {
	public static Main instance;
	private Scoreboard scoreboard;
	private ConfigManager configManager;
	private WebUtil webUtil;
	
	@Override
	public void onEnable() {
		instance = this;
		this.registerScoreboard();
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		this.webUtil = new WebUtil();
		this.registerScoreboard();
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
	
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	public ConfigManager getConfigManager() {
		return this.configManager;
	}
	
	public void reloadConfigManager() {
		this.saveConfig();
		this.reloadConfig();
		this.configManager = new ConfigManager(this);
	}
	
	private void registerScoreboard() {
		this.scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
	}
	
	public WebUtil getWebUtil() {
		return this.webUtil;
	}
}
