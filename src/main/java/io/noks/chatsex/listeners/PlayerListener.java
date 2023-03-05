package io.noks.chatsex.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.noks.chatsex.Main;
import io.noks.chatsex.manager.PlayerManager;
import io.noks.chatsex.utils.WebUtil;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerListener implements Listener {
	private Main main;
	public PlayerListener(Main plugin) {
		this.main = plugin;
	    this.main.getServer().getPluginManager().registerEvents(this, this.main);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (this.main.getConfigManager().isVoteSystemEnabled()) {
			this.checkLike(player);
		}
		new PlayerManager(player.getUniqueId()).update();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		PlayerManager.get(player.getUniqueId()).remove();
	}
	
	private void checkLike(Player player) {
		final String domainName = this.main.getConfigManager().getDomainName();
		if (domainName == null) return;
		final String rank = PermissionsEx.getPermissionManager().getUser(player).getParentIdentifiers().get(0);
		
        if (rank.equals("default") || rank.equals("verified")) {
            WebUtil.getResponse(this.main, "https://api.namemc.com/server/" + domainName + "/votes?profile=" + player.getUniqueId(), response -> {
            	switch (response) {
            	case "false":
            		if (rank.equals("verified")) {
            			PermissionsEx.getPermissionManager().getUser(player).removeGroup("verified");
            			PermissionsEx.getPermissionManager().getUser(player).addGroup("default");
            			player.sendMessage(ChatColor.RED + "Your rank has been removed due to your unlike!");
            		}
            		break;
            	case "true":
            		if (rank.equals("default")) {
            			PermissionsEx.getPermissionManager().getUser(player).removeGroup("default");
            			PermissionsEx.getPermissionManager().getUser(player).addGroup("verified");
            			player.sendMessage(ChatColor.GREEN + "Thanks for liking the server on NameMC! You've now got the Verified rank.");
            		}
            		break;
            	}
            });
        }
	}
}
