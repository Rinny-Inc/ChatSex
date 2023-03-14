package io.noks.chatsex.manager;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;

import io.noks.chatsex.Main;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerManager {
	private static final Map<UUID, PlayerManager> players = Maps.newConcurrentMap();
	private Player player;
	private UUID playerUUID;
	private String prefix, suffix;
	
	public PlayerManager(UUID playerUUID) {
	    this.playerUUID = playerUUID;
	    this.player = Bukkit.getPlayer(this.playerUUID);
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
	
	private String oldRankName = null;
	public void update() {
		if (this.prefix == null || this.prefix != PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix()) {
			this.prefix = PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix();
			this.player.setDisplayName(getColoredPrefix() + getPrefixColors() + getPlayer().getName());
			if (!Main.instance.getConfigManager().useScoreboardTeam()) {
				this.player.setPlayerListName(getPrefixColors() + this.player.getName());
			} else {
				final String rankName = PermissionsEx.getPermissionManager().getUser(getPlayer()).getGroups()[0].getName();
				Team team;
				if (rankName != oldRankName) {
					this.oldRankName = rankName;
					team = Main.instance.getScoreboard().getTeam(this.oldRankName);
					team.removePlayer(this.player);
				}
				if ((team = Main.instance.getScoreboard().getTeam(rankName)) == null) {
					team = Main.instance.getScoreboard().registerNewTeam(rankName);
				}
				// Need to keep this out of the if
				team.setPrefix(getPrefixColors());
				team.addPlayer(this.player);
			}
		}
		if (this.suffix == null || this.suffix != PermissionsEx.getPermissionManager().getUser(getPlayer()).getSuffix()) {
			this.suffix = PermissionsEx.getPermissionManager().getUser(getPlayer()).getSuffix();
			this.player.setDisplayName(this.player.getDisplayName() + getColoredSuffix());
		}
	}
	
	public String getColoredPrefix() {
		return ChatColor.translateAlternateColorCodes('&', getPrefix());
	}
	
	public String getPrefixColors() {
		if (getPrefix().isEmpty()) {
			return "";
		}
		char code = 'f';
		int count = 0;

		for (String string : getPrefix().split("&")) {
			if (string.isEmpty()) {
				continue;
			}
			if (ChatColor.getByChar(string.toCharArray()[0]) == null) {
				continue;
			}
			if (count == 0 && !isMagicColor(string.toCharArray()[0])) {
				code = string.toCharArray()[0];
				count++;
				continue;
			}
			if (count == 1 && isMagicColor(string.toCharArray()[0])) {
				return ChatColor.getByChar(code).toString() + ChatColor.getByChar(string.toCharArray()[0]).toString();
			}
		}
		return ChatColor.getByChar(code).toString();

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
