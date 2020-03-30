package com.ten31f.discord.elements;

import java.util.HashMap;
import java.util.Map;

import com.ten31f.discord.exceptions.GameStateException;
import com.ten31f.discord.exceptions.NotEnoughPlayersException;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Game {

	public static final int MINIMUM_PLAYERS = 5;

	public static enum GameState {
		JOIN, SETUP, DAY, NIGHT, END;
	};

	public Game(MessageChannel messageChannel) {
		setMessageChannel(messageChannel);
	}

	private Map<String, User> players = null;
	private MessageChannel messageChannel = null;
	private GameState gameState = GameState.JOIN;

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

	public int playerCount() {
		if (getPlayers() == null)
			return 0;

		return getPlayers().size();
	}

	public void allIn() throws NotEnoughPlayersException, GameStateException {

		if (!GameState.JOIN.equals(getGameState()))
			throw new GameStateException("This game is already started");

		if (playerCount() < MINIMUM_PLAYERS)
			throw new NotEnoughPlayersException(
					String.format("We need more players to start we only have %s/%s", playerCount(), MINIMUM_PLAYERS));

		setGameState(GameState.SETUP);
	}

	public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
