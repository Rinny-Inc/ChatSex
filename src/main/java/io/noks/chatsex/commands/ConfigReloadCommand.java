package io.noks.chatsex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.noks.chatsex.Main;

public class ConfigReloadCommand implements CommandExecutor {
	private final Main main;
	public ConfigReloadCommand(Main main) {
		this.main = main;
		main.getCommand("reloadconfig").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp()) {
			return false;
		}
		if (args.length > 0) {
			return false;
		}
		this.main.reloadConfigManager();
		sender.sendMessage(this.main.getName() + " Config reloaded");
		return true;
	}
}
