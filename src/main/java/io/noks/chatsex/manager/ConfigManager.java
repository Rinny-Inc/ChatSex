package io.noks.chatsex.manager;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import io.noks.chatsex.Main;

public class ConfigManager {
	private String domainName = "noks.io";
	private boolean voteSystem = false;
	private boolean scoreboardTeam = false, updatePersonal = false;
	private String chatFormat = "<%1$s> %2$s", defaultRank = "default", verifiedRank = "verified";
	
	public ConfigManager(Main main) {
		this.domainName = main.getConfig().getString("domain-name", this.domainName);
		this.voteSystem = main.getConfig().getBoolean("enable-vote-system", this.voteSystem);
		this.scoreboardTeam = main.getConfig().getBoolean("enable-scoreboard-team", this.scoreboardTeam);
		if (!this.scoreboardTeam) {
			Scoreboard mainSb = main.getServer().getScoreboardManager().getMainScoreboard();
			if (mainSb != null && !mainSb.getTeams().isEmpty()) {
				for (Team team : mainSb.getTeams()) {
					team.unregister();
				}
			}
		}
		this.updatePersonal = main.getConfig().getBoolean("update-personal-scoreboard", this.updatePersonal);
		this.chatFormat = main.getConfig().getString("chat-format", this.chatFormat);
		this.defaultRank = main.getConfig().getString("rank.default", this.defaultRank);
		this.verifiedRank = main.getConfig().getString("rank.when-liked", this.verifiedRank);
	}
	
	public String getDomainName() {
		return this.domainName;
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
	
	public String getDefaultRank() {
		return this.defaultRank;
	}
	
	public boolean updatePersonalScoreboard() {
		return this.updatePersonal;
	}
	
	public String getVerifiedRank() {
		return this.verifiedRank;
	}
}
