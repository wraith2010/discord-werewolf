package com.ten31f.discord.repo;

import java.util.HashMap;
import java.util.Map;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.exceptions.NoGameException;

import net.dv8tion.jda.api.entities.MessageChannel;

public class GamesRepo {

	private Map<String, Game> games = null;

	private Map<String, Game> getGames() {
		return games;
	}

	private void setGames(Map<String, Game> games) {
		this.games = games;
	}

	public Game newGame(MessageChannel messageChannel) {

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

	public Game removeGame(MessageChannel messageChannel) throws NoGameException {

		if (getGames() == null) {
			throw new NoGameException();
		}

		if (!getGames().containsKey(messageChannel.getId())) {
			throw new NoGameException();
		}

		return getGames().remove(messageChannel.getId());

	}

	public Game getGame(MessageChannel messageChannel) throws NoGameException {
		if (getGames() == null) {
			throw new NoGameException();
		}

		if (!getGames().containsKey(messageChannel.getId())) {
			throw new NoGameException();
		}

		return getGames().get(messageChannel.getId());
	}

}
