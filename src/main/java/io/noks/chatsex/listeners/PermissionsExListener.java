package io.noks.chatsex.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.noks.chatsex.Main;
import io.noks.chatsex.manager.PlayerManager;
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionEntityEvent.Action;

public class PermissionsExListener implements Listener {
	
	public PermissionsExListener(Main plugin) {
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onGroupChange(PermissionEntityEvent event) { 
		if (event.getAction() == Action.INHERITANCE_CHANGED) {
			final UUID uuid = Bukkit.getPlayer(event.getEntity().getName()).getUniqueId();
			PlayerManager.get(uuid).update();
		}
	}
}
