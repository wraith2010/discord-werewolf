package com.ten31f.discord.elements;

public enum Role {

	SEER("The Seer",
			"Once a night you select a player and the moderator tells you if they are a Villager or a Werewolf"),
	WEREWOLF("Werewolf", "Once a night you and the other werewolves select a victim"),
	VILLAGER("Villager", "You taste good to Werewolves. Good Luck!");

	private Role(String name, String power) {
		this.name = name;
		this.power = power;
	}

	private String name = null;
	private String power = null;

	public String getName() {
		return name;
	}

	public String getPower() {
		return power;
	}

}
