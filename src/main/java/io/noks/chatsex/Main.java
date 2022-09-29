package io.noks.chatsex;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.noks.chatsex.listeners.BlockListener;
import io.noks.chatsex.listeners.ChatListener;
import io.noks.chatsex.listeners.PlayerListener;

public class Main extends JavaPlugin {
	public static Main instance;
	public String domainName = "noks.io";
	
	@Override
	public void onEnable() {
		instance = this;
		this.registerListener();
	}
	
	private void registerListener() {
		new PlayerListener(this);
		new ChatListener(this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
}
