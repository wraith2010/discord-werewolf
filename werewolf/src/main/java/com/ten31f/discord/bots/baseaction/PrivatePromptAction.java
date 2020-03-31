package com.ten31f.discord.bots.baseaction;

import com.ten31f.discord.elements.Game;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class PrivatePromptAction extends ListenerAdapter {

	private boolean satisfied = false;
	private User user = null;
	private Game game = null;
	private PrivateChannel privateChannel = null;

	abstract public void prompt();

	abstract public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent);

	public PrivatePromptAction(User user, Game game) {
		setUser(user);
		setGame(game);
	}

	public boolean isSatisfied() {
		return satisfied;
	}

	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PrivateChannel getPrivateChannel() {
		if (privateChannel == null) {
			setPrivateChannel(getUser().openPrivateChannel().complete());
		}
		return privateChannel;
	}

	private void setPrivateChannel(PrivateChannel privateChannel) {
		this.privateChannel = privateChannel;
	}

	public Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

}
