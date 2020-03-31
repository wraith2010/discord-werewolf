package com.ten31f.discord.elements;

import net.dv8tion.jda.api.entities.User;

public class Player {

	private User user = null;
	private boolean alive = true;
	private Role role = null;

	public Player(User user) {
		setUser(user);
	}
	
	public User getUser() {
		return user;
	}

	private void setUser(User user) {
		this.user = user;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
