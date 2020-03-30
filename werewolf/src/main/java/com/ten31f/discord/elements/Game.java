package com.ten31f.discord.elements;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Game {

	public Game(MessageChannel messageChannel) {
		setMessageChannel(messageChannel);
	}

	private Map<String, User> players = null;
	private MessageChannel messageChannel = null;

	public Map<String, User> getPlayers() {
		return players;
	}

	private void setPlayers(Map<String, User> players) {
		this.players = players;
	}

	public boolean addPlayer(User user) {

		if (getPlayers() == null)
			setPlayers(new HashMap<String, User>());

		if (!getPlayers().containsKey(user.getId())) {
			getPlayers().put(user.getId(), user);
			return true;
		}

		return false;
	}

	public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

}
