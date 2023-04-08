package io.noks.chatsex.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.noks.chatsex.Main;
import io.noks.chatsex.manager.PlayerManager;
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionSystemEvent;

public class PermissionsExListener implements Listener {
	
	public PermissionsExListener(Main plugin) {
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onGroupChange(PermissionEntityEvent event) { 
		if (event.getAction() != PermissionEntityEvent.Action.INHERITANCE_CHANGED) {
			return;
		}
		final Player player = Bukkit.getPlayer(event.getEntity().getName());
		if (player == null) {
			return;
		}
		PlayerManager.get(player.getUniqueId()).update();
	}
	
	@EventHandler
	public void onPexReload(PermissionSystemEvent event) { 
		if (event.getAction() != PermissionSystemEvent.Action.RELOADED) {
			return;
		}
		if (PlayerManager.players.isEmpty()) {
			return;
		}
		for (PlayerManager pm : PlayerManager.players.values()) {
			pm.update();
		}
	}
}
