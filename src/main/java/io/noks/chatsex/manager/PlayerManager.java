package io.noks.chatsex.manager;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerManager {
	private static final Map<UUID, PlayerManager> players = Maps.newConcurrentMap();
	private Player player;
	private UUID playerUUID;
	private String prefix, suffix;
	
	public PlayerManager(UUID playerUUID) {
	    this.playerUUID = playerUUID;
	    this.player = Bukkit.getPlayer(this.playerUUID);
	    this.prefix = PermissionsEx.getPermissionManager().getUser(this.player).getPrefix();
	    this.suffix = PermissionsEx.getPermissionManager().getUser(this.player).getSuffix();
	    players.putIfAbsent(playerUUID, this);
	}

	public static PlayerManager get(UUID playerUUID) {
		if (!players.containsKey(playerUUID)) {
			return null;
		}
		return players.get(playerUUID);
	}
	public void remove() {
		players.remove(this.playerUUID);
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public UUID getPlayerUUID() {
		return this.playerUUID;
	}
	
	public String getPrefix() {
		this.update();
		return this.prefix;
	}
	
	private void update() {
		if (this.prefix != PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix()) {
			this.prefix = PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix();
			this.player.setPlayerListName(getPrefixColors() + getPlayer().getName());
			this.player.setDisplayName(getColoredPrefix() + getPrefixColors() + getPlayer().getName());
		}
	}
	
	public String getColoredPrefix() {
		return ChatColor.translateAlternateColorCodes('&', getPrefix());
	}
	
	public String getPrefixColors() {
		if (getPrefix().isEmpty()) {
			return "";
		}

		ChatColor color;
		ChatColor magicColor;

		char code = 'f';
		char magic = 'f';
		int count = 0;

		for (String string : getPrefix().split("&")) {
			if (!(string.isEmpty())) {
				if (ChatColor.getByChar(string.toCharArray()[0]) != null) {
					if (count == 0 && !isMagicColor(string.toCharArray()[0])) {
						code = string.toCharArray()[0];
						count++;
					} else if (count == 1 && isMagicColor(string.toCharArray()[0])) {
						magic = string.toCharArray()[0];
						count++;
					}
				}
			}
		}

		color = ChatColor.getByChar(code);
		magicColor = ChatColor.getByChar(magic);
		return count == 1 ? color.toString() : color.toString() + magicColor.toString();

		//                 |Tab||   Chat Prefix    |
		//                 |   ||                  |
		// PREFIX FORMAT -> &3&l&f[&3Developer&f] &3
	}
	
	public String getSuffix() {
		return this.suffix;
	}
	
	public String getColoredSuffix() {
		return ChatColor.translateAlternateColorCodes('&', getSuffix());
	}
	
	private boolean isMagicColor(char letter) {
		switch (letter) {
		case 'k':
			return true;
		case 'l':
			return true;
		case 'm':
			return true;
		case 'n':
			return true;
		case 'o':
			return true;
		case 'r':
			return true;
		default:
			break;
		}
		return false;
	}
	
	public boolean hasVoted() {
		return false;
	}
}
