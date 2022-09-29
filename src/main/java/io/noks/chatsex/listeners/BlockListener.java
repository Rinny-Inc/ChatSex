package io.noks.chatsex.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void translateColorWhenSignPlaced(SignChangeEvent event) {
		if (!event.getPlayer().isOp()) {
			return;
		}
		for (int i = 0; i < 4; i++) {
			if (event.getLine(i).length() == 0) continue;
			event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));  
		}
	}
}
