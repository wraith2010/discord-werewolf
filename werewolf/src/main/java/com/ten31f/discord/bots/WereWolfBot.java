package com.ten31f.discord.bots;

import java.util.HashMap;
import java.util.Map;

import com.ten31f.discord.elements.Game;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WereWolfBot extends ListenerAdapter {

	private static final String PHRASE_START_GAME = "!Want to play werewolf?";
	private static final String PHRASE_KILL_GAME = "!end werewolf";
	private static final String PHRASE_JOIN_GAME = "!join";

	private static final String RESPONSE_STARTING = "Pitter patter lets get at’er! You have %s to join";
	private static final String RESPONSE_NO_GAME = "Whats the big idea %s? We aren't even playing yet!";
	private static final String RESPONSE_PlAYER_ADD = "Woot!: %s is in!!!";
	private static final String RESPONSE_PlAYER_ALREADY = "Relax %s. Your already in.";

	private Map<String, Game> games = null;

	@Override
	public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {

		Message message = messageReceivedEvent.getMessage();

		switch (message.getContentRaw()) {
		case PHRASE_START_GAME:
			startGame(messageReceivedEvent);
			break;
		case PHRASE_KILL_GAME:
			killGame(messageReceivedEvent);
			break;
		case PHRASE_JOIN_GAME:
			joinGame(messageReceivedEvent);
			break;
		default:
		}

	}

	private void startGame(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();

		Game game = addGame(messageChannel);
		if (game == null) {

			messageChannel.sendMessage("Sorry only one game of werewolf at time per channel").queue();

			return;
		}

		User user = messageReceivedEvent.getAuthor();

		game.addPlayer(user);

		StringBuilder stringBuilder = new StringBuilder(RESPONSE_STARTING);
		stringBuilder.append("\n");
		stringBuilder.append(String.format("Who's in beside: %s\n", user.getName()));
		stringBuilder.append("We need atleast 7\n");
		stringBuilder.append("Type: !join to join in you have 2 minutes to join");

		messageChannel.sendMessage(stringBuilder).queue();
	}

	private void killGame(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();
		Game game = removeGame(messageChannel);

		User user = messageReceivedEvent.getAuthor();

		if (game == null) {

			messageChannel.sendMessage(String.format(RESPONSE_NO_GAME, user.getName())).queue();
			return;
		}

		messageChannel.sendMessage(String.format("%s says its time to go home. Game over!", user.getName())).queue();

	}

	private void joinGame(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();
		User user = messageReceivedEvent.getAuthor();

		Boolean result = addPlayer(messageChannel, user);

		if (result == null) {
			messageChannel.sendMessage(String.format(RESPONSE_NO_GAME, user.getName())).queue();
		} else if (result) {
			messageChannel.sendMessage(String.format(RESPONSE_PlAYER_ADD, user.getName())).queue();
		} else {
			messageChannel.sendMessage(String.format(RESPONSE_PlAYER_ALREADY, user.getName())).queue();
		}

	}

	private Game removeGame(MessageChannel messageChannel) {
		if (getGames() == null) {
			return null;
		}

		if (!getGames().containsKey(messageChannel.getId())) {
			return null;
		}

		return getGames().remove(messageChannel.getId());

	}

	private Game addGame(MessageChannel messageChannel) {

		if (getGames() == null) {
			setGames(new HashMap<String, Game>());
		}

		if (getGames().containsKey(messageChannel.getId())) {
			return null;
		}

		Game game = new Game(messageChannel);
		getGames().put(messageChannel.getId(), game);

		return game;

	}

	private Boolean addPlayer(MessageChannel messageChannel, User user) {

		if (getGames() == null)
			return null;
		if (!getGames().containsKey(messageChannel.getId()))
			return null;

		Game game = getGames().get(messageChannel.getId());
		return game.addPlayer(user);
	}

	private Map<String, Game> getGames() {
		return games;
	}

	private void setGames(Map<String, Game> games) {
		this.games = games;
	}

}
