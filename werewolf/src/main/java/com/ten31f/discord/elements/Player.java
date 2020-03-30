package com.ten31f.discord.elements;

public class Player {

	private boolean alive = true;
	private Role role = null;

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
