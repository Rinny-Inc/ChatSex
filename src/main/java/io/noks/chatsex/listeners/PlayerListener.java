package io.noks.chatsex.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.noks.chatsex.Main;
import io.noks.chatsex.manager.PlayerManager;
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
		final UUID playerUUID = event.getPlayer().getUniqueId();
		this.doDisconectionAction(playerUUID);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		final UUID playerUUID = event.getPlayer().getUniqueId();
		this.doDisconectionAction(playerUUID);
	}
	
	private void doDisconectionAction(UUID playerUUID) {
		final PlayerManager pm = PlayerManager.get(playerUUID);
		if (pm != null) {
			pm.remove();
		}
	}
	
	private void checkLike(Player player) {
		final String domainName = this.main.getConfigManager().getDomainName();
		if (domainName == null) return;
		final String rank = PermissionsEx.getPermissionManager().getUser(player).getParentIdentifiers().get(0);
		
        if (rank.equals(this.main.getConfigManager().getDefaultRank()) || rank.equals(this.main.getConfigManager().getVerifiedRank())) {
            this.main.getWebUtil().getResponse(this.main, "https://api.namemc.com/server/" + domainName + "/votes?profile=" + player.getUniqueId(), response -> {
            	switch (response) {
            	case "false":
            		if (rank.equals("verified")) {
            			PermissionsEx.getPermissionManager().getUser(player).removeGroup(this.main.getConfigManager().getVerifiedRank());
            			PermissionsEx.getPermissionManager().getUser(player).addGroup(this.main.getConfigManager().getDefaultRank());
            			player.sendMessage(ChatColor.RED + "You've been demoted to the player rank because you have removed your like on our NameMC page!");
            		}
            		break;
            	case "true":
            		if (rank.equals("default")) {
            			PermissionsEx.getPermissionManager().getUser(player).removeGroup(this.main.getConfigManager().getDefaultRank());
            			PermissionsEx.getPermissionManager().getUser(player).addGroup(this.main.getConfigManager().getVerifiedRank());
            			player.sendMessage(ChatColor.GREEN + "Thanks for your like on our NameMC page! You've been promoted to the " + this.main.getConfigManager().getVerifiedRank() + " rank.");
            		}
            		break;
            	}
            });
        }
	}
}
