package io.noks.chatsex;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import io.noks.chatsex.listeners.BlockListener;
import io.noks.chatsex.listeners.ChatListener;
import io.noks.chatsex.listeners.PlayerListener;
import io.noks.chatsex.manager.ConfigManager;

public class Main extends JavaPlugin {
	public static Main instance;
	public String domainName = "noks.io";
	private Scoreboard scoreboard;
	private ConfigManager configManager;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		this.registerListener();
		this.scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
		this.configManager = new ConfigManager(this);
	}
	
	private void registerListener() {
		new PlayerListener(this);
		new ChatListener(this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
	
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	public ConfigManager getConfigManager() {
		return this.configManager;
	}
}
