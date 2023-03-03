package io.noks.chatsex.manager;

import org.bukkit.scoreboard.Team;

import io.noks.chatsex.Main;

public class ConfigManager {
	private boolean voteSystem = false;
	private boolean scoreboardTeam = false;
	private String chatFormat = "<%1$s> %2$s";
	
	public ConfigManager(Main main) {
		this.voteSystem = main.getConfig().getBoolean("enable-vote-system", this.voteSystem);
		this.scoreboardTeam = main.getConfig().getBoolean("enable-scoreboard-team", this.scoreboardTeam);
		if (!this.scoreboardTeam) {
			if (main.getScoreboard().getTeams().isEmpty()) {
				return;
			}
			for (Team team : main.getScoreboard().getTeams()) {
				team.unregister();
			}
		}
		this.chatFormat = main.getConfig().getString("chat-format", this.chatFormat);
	}
	
	public boolean isVoteSystemEnabled() {
		return this.voteSystem;
	}
	
	public boolean useScoreboardTeam() {
		return this.scoreboardTeam;
	}
	
	public String getChatFormat() {
		return this.chatFormat;
	}
}
