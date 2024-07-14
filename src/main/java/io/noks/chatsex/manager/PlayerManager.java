package io.noks.chatsex.manager;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;

import io.noks.chatsex.Main;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerManager {
	public static final Map<UUID, PlayerManager> players = Maps.newConcurrentMap();
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
		// TODO: Personnal Prefix and Suffix
		if (this.prefix == null || this.prefix != PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix()) {
			this.prefix = PermissionsEx.getPermissionManager().getUser(getPlayer()).getPrefix();
			this.player.setDisplayName(getColoredPrefix() + getPlayer().getName());
			this.player.setPlayerListName(getPrefixColors() + this.player.getName(), true);
			Main main = Main.instance;
			if (main.getConfigManager().useScoreboardTeam()) {
				final String rankName = PermissionsEx.getPermissionManager().getUser(getPlayer()).getGroups()[0].getName();
				final Scoreboard mainSb = main.getServer().getScoreboardManager().getMainScoreboard();
				Team team;
				if (rankName != oldRankName) {
					this.oldRankName = rankName;
					if ((team = mainSb.getTeam(this.oldRankName)) != null) {
						team.removePlayer(this.player);
					}
				}
				if ((team = mainSb.getTeam(rankName)) == null) {
					team = mainSb.registerNewTeam(rankName);
				}
				// Need to keep this out of the if
				team.setPrefix(getPrefixColors());
				if (!team.hasPlayer(this.player)) {
					team.addPlayer(this.player);
				}
				/*if (main.getConfigManager().updatePersonalScoreboard()) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							for (Player players : main.getServer().getOnlinePlayers()) {
								Scoreboard playerBoard = players.getScoreboard();
								if (playerBoard == null || playerBoard == mainSb) {
									continue;
								}
								for (Team teams : mainSb.getTeams()) {
									String teamName = teams.getName();
									if (teamName == null) {
										continue;
									}
									Team newTeam = playerBoard.getTeam(teamName);
									if (newTeam == null) {
										newTeam = playerBoard.registerNewTeam(teamName);
										newTeam.setPrefix(teams.getPrefix());
									}
									for (OfflinePlayer entry : teams.getPlayers()) {
										if (newTeam.hasPlayer(entry)) {
											continue;
										}
					                    newTeam.addPlayer(entry);
					                }
								}
							}
						}
					}.runTask(main);
				}*/
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
			if (string.isEmpty() || ChatColor.getByChar(string.toCharArray()[0]) == null) {
				continue;
			}
			switch (count) {
				case 0: {
					if (!isMagicColor(string.toCharArray()[0])) {
						code = string.toCharArray()[0];
						count++;
						continue;
					}
				}
				case 1: {
					if (isMagicColor(string.toCharArray()[0])) {
						return ChatColor.getByChar(code).toString() + ChatColor.getByChar(string.toCharArray()[0]).toString();
					}
				}
				default: {
					break;
				}
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
