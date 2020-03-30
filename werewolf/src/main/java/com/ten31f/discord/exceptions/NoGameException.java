package com.ten31f.discord.exceptions;

import net.dv8tion.jda.api.entities.User;

public class NoGameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String RESPONSE_NO_GAME = "Whats the big idea %s? We aren't even playing yet!";

	public static String generateMessage(User user) {
		return String.format(RESPONSE_NO_GAME, user.getName());
	}

}
